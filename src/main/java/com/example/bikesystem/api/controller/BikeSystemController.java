package com.example.bikesystem.api.controller;


import com.example.bikesystem.api.dto.request.SimulateRequestDTO;
import com.example.bikesystem.api.dto.response.*;
import com.example.bikesystem.api.service.BikeSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 카카오 T 바이크 관리 시뮬레이션
 * 카카오 T 바이크는 카카오 모빌리티에서 제공하는 전기 자전거 대여 서비스이다.
 * 서비스의 문제점은 어떤 지역은 대여 수요는 많으나 반납되는 자전거가 없어 자전거가 부족하고,
 * 반대로 어떤 지역은 반납되는 자전거는 많지만 대여 수요가 없어 자전거가 쌓이기만 한다는 것이다.
 * 이런 현상을 해결하기 위해 신입사원 죠르디에게는 트럭으로 자전거를 재배치하라는 업무가 주어졌다.
 * 지원자는 죠르디를 도와 자전거가 많이 대여될 수 있도록 트럭을 운영해보자.
 *
 * 트럭 운영 목표
 * 목표: 트럭을 효율적으로 운영하여 대여소에 취소되는 대여 요청 수를 최대한 줄여야 한다.
 * 단, 트럭은 적게 움직이는 편이 좋다.
 *
 * 서비스 지역 및 제약 사항 설명
 * 서비스 지역
 * 자전거를 대여하고 반납할 수 있는 서비스 지역은 NxN 크기의 정방형 격자 모양으로 주어진다. 각 칸의 한 변의 길이는 100m이며,
 * 한 칸에는 자전거 대여소가 하나씩 있다. 자전거 대여소에는 0부터 (NxN) - 1까지의 ID가 지정되어 있다. ID 값은 격자의 좌하단부터 0에서 시작하여 하나씩 증가하는 일련번호이다.
 *
 * 다음은 5X5 크기 서비스 지역의 예시이다.
 *
 * 서버의 처리 순서
 * 서버는 작업을 다음 순으로 처리한다.
 * 정시 분(minute)마다 사용자가 대여 요청을 할 때 받은 정보를 이용해, 사용 시간이 다 된 자전거를 반납 처리한다.
 * 정시 분(minute)마다 사용자가 보낸 새로운 대여 요청을 순서대로 처리한다.
 * 예를 들어 자전거가 1대 있는 자전거 대여소에 대여 요청이 2개 들어온 경우 json array(하단의 과거 대여 요청 기록 참고)의 앞쪽에 위치한 대여 요청에게 자전거를 빌려주고, 뒤에 있는 대여 요청은 취소한다.
 * 죠르디의 지시에 따라 트럭을 운행한다.
 *
 *
 * 시나리오 1(problem = 1)
 *
 * 서비스 지역의 크기: 5X5
 * 자전거 대여 요청 빈도: 분당 요청 수 평균 2건
 * 자전거 수: 100대. 각 자전거 대여소에는 초기에 자전거가 4대씩 있음
 * 트럭 수: 5대
 *
 *
 * 시나리오 2(problem = 2)
 * 서비스 지역의 크기: 60X60
 * 자전거 대여 요청 빈도: 분당 요청 수 평균 15건
 * 자전거 수: 10,800대. 각 자전거 대여소에는 초기에 자전거가 3대씩 있음
 * 트럭 수: 10대
 * [주의] 시나리오 2의 HotPlace 안내
 *
 * 시나리오 2에는 대여 요청이 가장 많이 일어나는 대여소와 반납이 가장 많이 일어나는 선호 대여소가 각각 1개씩 정해져 있다.
 * 이 선호 패턴은 4시간(=240분)마다 변경되므로, 즉 12시간(=720분) 동안 서로 다른 패턴 3개를 볼 수 있다.
 * 선호도가 가장 높은 대여소와 선호도가 가장 낮은 대여소의 대여 요청/반납 건수는 약 60배 차이를 보인다.
 * 선호도는 최근 3일 동안의 대여 요청을 기준으로 결정되며 선호도를 알아내려면 아래 과거 대여 요청 기록을 참고하라.
 *
 * 과거 대여 요청 기록
 * 아래의 파일에는 하루 전, 이틀 전, 사흘 전에 들어온 대여 요청을 시각(분)별로 기록한 것이다. 각 파일은 다음과 같은 json format으로 되어있다.
 *
 * json	설명
 * Key	사용자의 요청이 들어온 시각
 * Value	해당 시각에 들어온 사용자의 요청 배열
 * Value 배열의 각 원소	[자전거를 대여할 자전거 대여소 ID, 자전거를 반납할 자전거 대여소 ID, 자전거를 탈 시간(분)]
 * Example
 *
 * {
 *   "0":[[13, 14, 51], [2, 14, 34], [8, 13, 31], [10, 20, 38]],
 *   "2":[[19, 16, 7]], // 카카오 T 바이크 서비스 시작 2분 후에 ID가 19인 자전거 대여소에서 자전거를 빌리고 7분 뒤 ID가 16인 자전거 대여소에 반납
 *   "3":[[15, 21, 51]],
 *   …(이하 생략)
 * }
 *
 * 시나리오 1(HotPlace 없음)
 * 1일 전: 다운로드
 * 2일 전: 다운로드
 * 3일 전: 다운로드
 * 시나리오 2
 * 1일 전: 다운로드
 * 2일 전: 다운로드
 * 3일 전: 다운로드
 * 점수
 * 점수는 시나리오별로 다음과 같이 계산된다.
 *
 * 구분	공식
 * 달성률 점수	(성공한 요청 수 - S’) / (S - S’) x 100
 * 효율성 점수	(효율성 점수: (T - t) / T x 100
 * 총점	(달성률 점수) X 0.95 + (효율성 점수) X 0.05와 0 중 더 큰 값
 * ※ S = (사용자의 총 대여 요청 수)
 * ※ S’ = (트럭이 아무것도 안 했을 때 시나리오에서 성공하는 요청 수)
 * ※ T = (모든 트럭이 쉬지 않고 계속 달린다고 가정할 때 모든 트럭이 달린 거리의 합)
 * ※ t = (지원자의 시뮬레이션에서 모든 트럭이 달린 거리의 합)
 *
 * 구분	시나리오 1	시나리오 2
 * S	1428	10829
 * S’	1077	9142
 * T	3600(km)	7200(km)
 *
 * 참고
 * 완료 후 작성한 코드를 업로드해야 한다. 여러 파일일 경우 zip으로 압축하여 업로드한다. 시험 시간 내에 코드를 제출하지 않으면 획득한 점수는 무시된다.
 * 각 시나리오의 총점은 모든 시도 중 최고 점수로 반영된다.
 * 총점은 시뮬레이션에서 카카오 T 바이크 서버의 상태가 finished가 될 때 산출된다.
 * 최종 점수는 (시나리오 1에서 획득한 총점) x (시나리오 1의 배점 가중치) + (시나리오 2에서 획득한 총점) x (시나리오 2의 배점 가중치)로 부여된다. 단, 배점 가중치는 지원자에게 공개하지 않는다.
 *
 * 예시
 * 서비스 지역
 * 2X2 크기의 서비스 지역이 주어질 때, 각 자전거 대여소가 초기에 보유한 자전거 대수가 다음과 같다고 하자. 자전거 대수는 자전거 대여소의 ID 순으로 나열하였다.
 *
 * [2, 1, 1, 0]
 * ※ 실제 주어지는 데이터에서는 모든 자전거 대여소가 초기에 같은 수의 자전거를 갖고 있으나, 본 예시에서는 이해를 돕기 위해 변형을 주었다.
 *
 * 이를 그림으로 표현하면 다음과 같다.
 *
 * 사용자의 요청
 * 사용자의 요청이 다음과 같이 주어진다고 하자.
 *
 * 10시 00분: [[3, 0, 10]]
 * 10시 01분: [[1, 3, 1], [1, 4, 15]]
 * 10시 02분: [[0, 3, 2], [3, 1, 4], [0, 3, 1]]
 * 10시 03분: [[1, 3, 5]]
 * 각 원소는 각 사용자가 보낸 요청으로, [자전거를 대여할 자전거 대여소 ID, 자전거를 반납할 자전거 대여소 ID,
 * 자전거를 탈 시간(분 단위)]를 뜻한다.
 * 예를 들어 [3, 0, 10]은 ID가 3인 자전거 대여소에서 자전거를 대여하여, 10분 동안 자전거를 탄 후 ID가 0인 자전거 대여소에 자전거를 보관하겠다는 의미이다.
 *
 * 예시 1 - 죠르디가 아무 일도 하지 않음
 * 죠르디가 아무 일도 하지 않으면(즉, 트럭이 아무 일도 하지 않으면) 결과는 다음과 같다.
 *
 * 10시 00분
 *
 * 각 자전거 대여소에 있는 자전거 수: [2, 1, 1, 0]
 *
 * 자전거 반납: 반납된 자전거가 없다.
 * 자전거 대여:
 * 요청 1 [3, 0, 10]: ID 3인 자전거 대여소에는 자전거가 없으므로, 이 요청은 취소된다.
 * 트럭 운행: 트럭은 아무 일도 하지 않는다.
 * 취소된 요청 수: 1
 * 트럭이 움직인 거리: 0m
 * 대여 중인 자전거: 없음
 *
 * 10시 01분
 *
 * 각 자전거 대여소에 있는 자전거 수: [2, 1, 1, 0]
 *
 * 자전거 반납: 반납된 자전거가 없다.
 * 자전거 대여:
 * 요청 2 [1, 3, 1]: ID 1인 자전거 대여소는 자전거가 1대 있으므로, 이 요청은 수락된다.
 * 요청 3 [1, 4, 15]: 요청 2에 의해 ID 1인 자전거 대여소에는 자전거가 없으므로, 이 요청은 취소된다.
 * 트럭 운행: 트럭은 아무 일도 하지 않는다.
 * 취소된 요청 수: 2
 * 트럭이 움직인 거리: 0m
 * 대여 중인 자전거:
 *     * 요청 2, 10시 02분에 ID 3인 자전거 대여소에 반납 예정
 *
 * 10시 02분
 *
 * 각 자전거 대여소에 있는 자전거 수: [2, 0, 1, 0]
 *
 * 자전거 반납:
 * 요청 2의 자전거가 ID가 3인 자전거 대여소로 반납된다.
 * 자전거 대여:
 * 요청 4 [0, 3, 2]: ID 0인 대여소에는 자전거가 2대 있으므로, 이 요청은 수락된다.
 * 요청 5 [3, 1, 4]: 반납에 의해 ID 3인 대여소에는 자전거가 1대 생겼으므로, 이 요청은 수락된다.
 * 요청 6 [0, 3, 1]: ID 0인 대여소에는 자전거가 1대 있으므로, 이 요청은 수락된다.
 * 트럭 운행: 트럭은 아무 일도 하지 않는다.
 * 취소된 요청 수: 2
 * 트럭이 움직인 거리: 0m
 * 대여 중인 자전거:
 *     * 요청 4, 10시 04분에 ID 3인 자전거 대여소에 반납 예정
 *     * 요청 5, 10시 06분에 ID 1인 자전거 대여소에 반납 예정
 *     * 요청 6, 10시 03분에 ID 3인 자전거 대여소에 반납 예정
 * 10시 03분
 *
 * 각 자전거 대여소에 있는 자전거 수: [0, 0, 1, 0]
 *
 * 자전거 반납:
 * 요청 6의 자전거가 ID가 3인 자전거 대여소로 반납된다.
 * 자전거 대여:
 * 요청 7 [1, 3, 5]: ID 1인 자전거 대여소에는 자전거가 없으므로, 이 요청은 취소된다.
 * 트럭 운행: 트럭은 아무 일도 하지 않는다.
 * 취소된 요청 수: 3
 * 트럭이 움직인 거리: 0m
 * 대여 중인 자전거:
 *     * 요청 4, 10시 04분에 ID 3인 자전거 대여소에 반납 예정
 *     * 요청 5, 10시 06분에 ID 1인 자전거 대여소에 반납 예정
 * 예시 2 - 죠르디가 트럭을 운행함
 * 죠르디가 10:00부터 트럭을 운행해, 트럭이 ID가 2인 자전거 대여소에 있는 자전거 한 대를 ID가 1인 자전거 대여소로 이송하면 결과는 다음과 같다.
 *
 * 10시 00분
 *
 * 각 자전거 대여소에 있는 자전거 수: [2, 1, 1, 0]
 *
 * 자전거 반납: 반납된 자전거가 없다.
 * 자전거 대여:
 * 요청 1 [3, 0, 10]: ID 3인 자전거 대여소에는 자전거가 없으므로, 이 요청은 취소된다.
 * 트럭 운행:
 * 10시 00분 00초 ~ 10시 00분 06초: ID 0 자전거 대여소에서 ID 2 자전거 대여소로 이동한다.
 * 10시 00분 06초 ~ 10시 00분 12초: ID 2 자전거 대여소에서 자전거를 한 대 싣는다.
 * 10시 00분 12초 ~ 10시 00분 24초: ID 2 자전거 대여소에서 ID 1 자전거 대여소로 이동한다.
 * 10시 00분 24초 ~ 10시 00분 30초: ID 1 자전거 대여소에 자전거를 한 대 내린다.
 * 취소된 요청 수: 1
 * 트럭이 움직인 거리: 300m
 * 대여 중인 자전거: 없음
 *
 * 10시 01분
 *
 * 각 자전거 대여소에 있는 자전거 수: [2, 2, 0, 0]
 *
 * 자전거 반납: 반납된 자전거가 없다.
 * 자전거 대여:
 * 요청 2 [1, 3, 1]: ID 1인 자전거 대여소는 자전거가 2대 있으므로, 이 요청은 수락된다.
 * 요청 3 [1, 4, 15]: ID 1인 자전거 대여소에는 자전거가 1대 있으므로, 이 요청은 수락된다.
 * 트럭 운행: 트럭은 아무 일도 하지 않는다.
 * 취소된 요청 수: 1
 * 트럭이 움직인 거리: 300m
 * 대여 중인 자전거:
 *     * 요청 2, 10시 02분에 ID 3인 자전거 대여소에 반납 예정
 *     * 요청 3, 10시 16분에 ID 4인 자전거 대여소에 반납 예정
 *
 * 10시 02분
 *
 * 각 자전거 대여소에 있는 자전거 수: [2, 0, 0, 0]
 *
 * 자전거 반납:
 * 요청 2의 자전거가 ID가 3인 자전거 대여소로 반납된다.
 * 자전거 대여:
 * 요청 4 [0, 3, 2]: ID 0인 자전거 대여소에는 자전거가 2대 있으므로, 이 요청은 수락된다.
 * 요청 5 [3, 1, 4]: 반납에 의해 ID 3인 자전거 대여소에는 자전거가 1대 생겼으므로, 이 요청은 수락된다.
 * 요청 6 [0, 3, 1]: ID 0인 대여소에는 자전거가 1대 있으므로, 이 요청은 수락된다.
 * 트럭 운행: 트럭은 아무 일도 하지 않는다.
 * 취소된 요청 수: 1
 * 트럭이 움직인 거리: 300m
 * 대여 중인 자전거:
 *     * 요청 3, 10시 16분에 ID 4인 자전거 대여소에 반납 예정
 *     * 요청 4, 10시 04분에 ID 3인 자전거 대여소에 반납 예정
 *     * 요청 5, 10시 06분에 ID 1인 자전거 대여소에 반납 예정
 *     * 요청 6, 10시 03분에 ID 3인 자전거 대여소에 반납 예정
 *
 * 10시 03분
 *
 * 각 자전거 대여소에 있는 자전거 수: [0, 0, 0, 0]
 *
 * 자전거 반납:
 * 요청 6의 자전거가 ID가 3인 자전거 대여소로 반납된다.
 * 자전거 대여:
 * 요청 7 [1, 3, 5]: ID 1인 자전거 대여소에는 자전거가 없으므로, 이 요청은 취소된다.
 * 트럭 운행: 트럭은 아무 일도 하지 않는다.
 * 취소된 요청 수: 2
 * 트럭이 움직인 거리: 300m
 * 대여 중인 자전거:
 *     * 요청 3, 10시 16분에 ID 4인 자전거 대여소에 반납 예정
 *     * 요청 4, 10시 04분에 ID 3인 자전거 대여소에 반납 예정
 *     * 요청 5, 10시 06분에 ID 1인 자전거 대여소에 반납 예정
 *
 *
 * API REFERENCE
 * HTTPS로 통신한다.
 *
 * 초당 10회가 넘는 API 호출은 서버가 응답하지 않을 수 있다.
 * BASE URL: https://kox947ka1a.execute-api.ap-northeast-2.amazonaws.com/prod/users
 * Response Codes
 * API를 성공적으로 호출할 경우 200 코드를 반환하고, 그 외의 경우에는 아래의 코드를 반환한다.
 *
 * Response Code	Description
 * 200 OK	성공
 * 400 Bad Request	Parameter가 잘못됨 (범위, 값 등)
 * 401 Unauthorized	인증을 위한 Header가 잘못됨
 * 500 Internal Server Error	서버 에러, 채팅으로 문의 요망
 *
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/bikesystem")
public class BikeSystemController {

    private final BikeSystemService bikeSystemService;


    /**
     * Request
     * POST /start
     * X-Auth-Token: {X-Auth-Token}
     * Content-Type: application/json
     * Header
     * <p>
     * Name	Description
     * X-Auth-Token	문제에서 발급되는 응시자 식별 토큰 값
     * Parameter
     * <p>
     * Name	Type	Description
     * problem	Integer	시나리오 번호 (1 <= problem <= 2)
     * Example
     * <p>
     * curl -X POST {BASE_URL}/start \
     * -H 'X-Auth-Token: {X_AUTH_TOKEN}' \
     * -H 'Content-Type: application/json' \
     * -d '{
     * "problem": 1
     * }'
     * Response
     * Key
     * <p>
     * Key	Type	Description
     * auth_key	String	Start API 를 통해 발급받은 key, 이후 문제 풀이에 진행되는 모든 API에 이 key를 사용
     * problem	Integer	선택한 시나리오 번호
     * time	Integer	현재 카카오 T 바이크 서비스에서의 시각 (0부터 시작)
     * Example
     * <p>
     * {
     * "auth_key": "1fd74321-d314-4885-b5ae-3e72126e75cc",
     * "problem": 1,
     * "time": 0
     * }
     */
    @PostMapping("/start")
    public SystemStartResponseDTO startBikeSystem(@RequestBody Map<String, String> reqDto, HttpServletRequest request, HttpServletResponse response) {
        return bikeSystemService.startBikeSystem(reqDto.get("problem"));
    }


    /**
     * Locations API
     * 현재 카카오 T 바이크 서비스 시각에 각 자전거 대여소가 보유한 자전거 수를 반환한다.
     * <p>
     * Request
     * GET /locations
     * Authorization: {auth_key}
     * Content-Type: application/json
     * Header
     * <p>
     * Name	Description
     * Authorization	Start API 에서 발급받은 auth_key
     * Example
     * <p>
     * curl -X GET {BASE_URL}/locations \
     * -H 'Authorization: {AUTH_KEY}' \
     * -H 'Content-Type: application/json'
     * Response
     * Key
     * <p>
     * Key	Type	Description
     * locations	Array of Location	각 자전거 대여소의 ID, 보유하고 있는 자전거 수에 대한 정보를 담은 배열
     * Example
     * <p>
     * {
     * "locations": [
     * { "id": 0, "located_bikes_count": 3 },
     * { "id": 1, "located_bikes_count": 3 },
     * ...
     * ]
     */

    @GetMapping("/locations")
    public LocationsResponseDTO getLocationInfo(HttpServletRequest request, HttpServletResponse response) {

        String authorization = request.getHeader("authorization");

        LocationsResponseDTO locationInfo = bikeSystemService.getLocationInfo(authorization);

        return locationInfo;
    }


    /**
     * Trucks API
     * 현재 카카오 T 바이크 서비스 시각에 각 트럭의 위치와 싣고 있는 자전거 수를 반환한다.
     * <p>
     * Request
     * GET /trucks
     * Authorization: {auth_key}
     * Content-Type: application/json
     * Header
     * <p>
     * Name	Description
     * Authorization	Start API 에서 발급받은 auth_key
     * Example
     * <p>
     * curl -X GET {BASE_URL}/trucks \
     * -H 'Authorization: {AUTH_KEY}' \
     * -H 'Content-Type: application/json'
     * Response
     * Key
     * <p>
     * Key	Type	Description
     * trucks	Array of Truck	각 트럭의 ID, 현재 위치, 싣고 있는 자전거 수에 대한 정보를 담은 배열
     * Example
     * <p>
     * {
     * "trucks": [
     * { "id": 0, "location_id": 0, "loaded_bikes_count": 0 },
     * { "id": 1, "location_id": 0, "loaded_bikes_count": 0 },
     * ...
     * ]
     * }
     */
    @GetMapping("/trucks")
    public TruckResponseDTO getTrucksInfo(HttpServletRequest request, HttpServletResponse response) {

        return bikeSystemService.getTrucksInfo(request.getHeader("authorization"));
    }


    /**
     *  json 설명
     *  Key	사용자의 요청이 들어온 시각
     *  Value	해당 시각에 들어온 사용자의 요청 배열
     *  Value 배열의 각 원소	[자전거를 대여할 자전거 대여소 ID, 자전거를 반납할 자전거 대여소 ID, 자전거를 탈 시간(분)]
     *  Example
     *       *  {
     *    "0":[[13, 14, 51], [2, 14, 34], [8, 13, 31], [10, 20, 38]],
     *    "2":[[19, 16, 7]], // 카카오 T 바이크 서비스 시작 2분 후에 ID가 19인 자전거 대여소에서 자전거를 빌리고 7분 뒤 ID가 16인 자전거 대여소에 반납
     *    "3":[[15, 21, 51]],
     *    …(이하 생략)
     *  }
     * @param reqDto
     * @param request
     * @param response
     * @return
     */


    /**
     * 1. 시간(key) , 자전거리스트(value) 분해 작업
     * 2. 요청 시간 기준을 key로 자전거리스트(Value)로 Map 구성
     * 3. 자전거에 반납 시간 Key 기준으로 자전거 리스트(value)로 Map 구성
     * @param reqDto
     * @param request
     * @param response
     * @return
     */

    @PostMapping("/pastRentRequests")
    public PastRentResponseDTO pastRentRequests(@RequestBody Map<String, List> reqDto, HttpServletRequest request, HttpServletResponse response) {

        return bikeSystemService.createRequestMap(reqDto);

    }




    @PostMapping("/rent")
    public RentResponseDTO rent(@RequestBody Map<String, List> reqDto, HttpServletRequest request, HttpServletResponse response) {

        return bikeSystemService.rent(reqDto);

    }



    /**
     * Simulate API
     *  현재 시각 ~ 현재 시각 + 1분 까지 각 트럭이 행할 명령을 담아 서버에 전달한다.
     *
     *    호출 시 서버에서는 다음과 같은 일이 진행된다.
     * 1. 카카오 T 바이크 서버의 상태가 in_progress 로 변경된다.(완료)
     * 2. 서버 내부에 기록된 returns(자전거 반납 예정 Hash) 를 확인하여 각 자전거 대여소가 가진 자전거 수를 늘린다.(다시 구현 필요)
     * 3. 서버 내부에 기록된 requests(자전거 대여 예정 Hash) 를 확인하여 각 자전거 대여소가 가진 자전거 수를 줄인다.(다시 구현 필요)
     * 4. 자전거 대여 요청이 취소될 경우 요청 취소 수를 1 증가시켜 서버에 저장한다.(완료)
     * 5. 자전거 대여 요청이 성공할 경우 현재 시각 + 대여 시간 을 key, 반납할 자전거 대여소 ID 를 value로 하여 returns 에 저장한다.
     * 6. 매개 변수로 받은 트럭의 명령들을 수행한다. 이때 각 시각별로 ID가 낮은 트럭의 명령부터 순서대로 처리한다.
     * 7. 명령이 자전거 상차 또는 자전거 하차 일 경우 트럭이 가진 자전거 수를 증가 또는 감소시킨다.(완료)
     * 8. 자전거가 없는 대여소에서 상차를 하거나, 트럭에 자전거가 없는데 하차를 하려는 명령은 무시된다. 이때 명령은 무시되지만 시간은 정상적으로 각기 6초가 소비된다.
     * 9. 명령이 상, 하, 좌, 우 이동일 경우 트럭의 이동 거리를 100m 증가시키고, 트럭의 위치를 기록한다.(완료)
     * 10. 서비스 지역을 벗어나는 이동 명령은 무시된다. 이때 명령은 무시되지만 시간은 정상적으로 각기 6초가 소비된다.
     * 11. 매개 변수로 받은 명령 중 1분 내에 할 수 있는 명령까지만 수행한다. 즉, 한 번의 simulate 요청 안에서 각 트럭에게 내릴 수 있는 명령 수는 최대 10개이고, 그 이상의 명령은 실행되지 않는다.
     *     * Simulate 요청이 성공하면 카카오 T 바이크 서버의 상태가 ready 로 변경되고, 현재 시각이 1분 증가한다.
     * 12. 한 시나리오에서 Simulate 요청이 720번 성공되면 카카오 T 바이크 서버의 상태는 ready가 아닌 finished로 변경되며, 총점이 반영된다.
     * 13. 721번 이상도 Simulate 요청을 보낼 순 있으나, 이때의 이동거리 등은 점수에 반영되지 않는다.
     * 14. 다시 같은 시나리오를 Simulate 하고 싶다면 Start API를 이용해 새로운 AUTH_KEY를 발급받아야 한다.
     *
     * <p>
     * Request
     * PUT /simulate
     * Authorization: {auth_key}
     * Content-Type: application/json
     * Header
     * <p>
     * Name	Description
     * Authorization	Start API 에서 발급받은 auth_key
     * Parameter
     * <p>
     * Name	Type	Description
     * commands	Array of Command	각 트럭이 현재 시각 ~ 현재 시각 + 1분 까지 수행할 명령
     * ※ 트럭의 명령
     * <p>
     * 0: 6초간 아무것도 하지 않음
     * 1: 위로 한 칸 이동
     * 2: 오른쪽으로 한 칸 이동
     * 3: 아래로 한 칸 이동
     * 4: 왼쪽으로 한 칸 이동
     * 5: 자전거 상차
     * 6: 자전거 하차
     * <p>
     * 예를 들어, `{ "truck_id": 0, "command": [2, 5, 4, 1, 6] }`으로 명령을 하면 ID가 0인 트럭은 아래와 같이 운행한다.
     * 1) 오른쪽으로 한 칸 이동
     * 2) 자전거 1대 상차
     * 3) 왼쪽으로 한 칸 이동
     * 4) 위로 한 칸 이동
     * 5) 자전거 1대 하차
     * Example
     * <p>
     * curl -X PUT {BASE_URL}/simulate \
     * -H 'Authorization: {AUTH_KEY}' \
     * -H 'Content-Type: application/json' \
     * -d '{
     * "commands": [
     * { "truck_id": 0, "command": [2, 5, 4, 1, 6] }
     * ...
     * ]
     * }'
     * Response
     * Key
     * <p>
     * Key	Type	Description
     * status	String	현재 카카오 T 서버의 상태
     * time	Integer	현재 시각 (요청 시각에서 1분 경과)
     * failed_requests_count	Integer	실패한 요청 수
     * distance	String	모든 트럭이 현재까지 이동한 거리의 총합(km 단위)
     * Example
     * <p>
     * {
     * "status": "ready",
     * "time": 1,
     * "failed_requests_count": 5,
     * "distance": 1.2,
     * }
     */
    @PutMapping("/simulate")
    public SimulateResponseDTO simulate(@RequestBody SimulateRequestDTO reqDto, HttpServletRequest request, HttpServletResponse response) {

        return bikeSystemService.simulate(reqDto);
    }



    /**
     * Score API
     *  해당 Auth key로 획득한 점수를 반환한다. 점수는 높을수록 좋다. 카카오 T 바이크 서버의 상태가 finished가 아닐 때 본 API를 호출하면 response의 score는 무조건 0.0이다.
     *       *  Request
     *  GET /score
     *  Authorization: {auth_key}
     *  Content-Type: application/json
     *  Header
     *       *  Name	Description
     *  Authorization	Start API 에서 발급받은 auth_key
     *  Example
     *       *  curl -X GET {BASE_URL}/score \
     *       -H 'Authorization: {AUTH_KEY}' \
     *       -H 'Content-Type: application/json'
     *  Response
     *  Key
     *       *  Key	Type	Description
     *  score	Float	획득한 점수
     *  Example
     *       *  {
     *    "score": 75.7
     *  }
     *  [1]: 실제 카카오 T 바이크에서 사용자는 애플리케이션과 자동 잠금장치를 이용하여 아무 곳에서나 자전거를 대여하고 반납할 수 있으나,
     *  이번 과제에서는 문제를 간단화하기 위하여 지정된 위치에서만 자전거를 대여하고 반납할 수 있다고 가정한다.
     */
}
