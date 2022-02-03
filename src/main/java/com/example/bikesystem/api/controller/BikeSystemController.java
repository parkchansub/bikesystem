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
 * HTTPS로 통신한다.
 * Response Codes
 * <p>
 * API를 성공적으로 호출할 경우 200 코드를 반환하고, 그 외의 경우에는 아래의 코드를 반환한다.
 * <p>
 * Response Code	Description
 * 200 OK	성공
 * 400 Bad Request	Parameter가 잘못됨 (범위, 값 등)
 * 401 Unauthorized	인증을 위한 Header가 잘못됨
 * 500 Internal Server Error	서버 에러, 채팅으로 문의 요망
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
     * Simulate API
     * 1. 현재 시각 ~ 현재 시각 + 1분 까지 각 트럭이 행할 명령을 담아 서버에 전달한다. 호출 시 서버에서는 다음과 같은 일이 진행된다.
     * 2. 카카오 T 바이크 서버의 상태가 in_progress 로 변경된다.(완료)
     * 3. 서버 내부에 기록된 returns(자전거 반납 예정 Hash) 를 확인하여 각 자전거 대여소가 가진 자전거 수를 늘린다.(다시 구현 필요)
     * 4. 서버 내부에 기록된 requests(자전거 대여 예정 Hash) 를 확인하여 각 자전거 대여소가 가진 자전거 수를 줄인다.(다시 구현 필요)
     * 5. 자전거 대여 요청이 취소될 경우 요청 취소 수를 1 증가시켜 서버에 저장한다.(완료)
     * 6. 자전거 대여 요청이 성공할 경우 현재 시각 + 대여 시간 을 key, 반납할 자전거 대여소 ID 를 value로 하여 returns 에 저장한다.
     * 7. 매개 변수로 받은 트럭의 명령들을 수행한다. 이때 각 시각별로 ID가 낮은 트럭의 명령부터 순서대로 처리한다.
     * 8. 명령이 자전거 상차 또는 자전거 하차 일 경우 트럭이 가진 자전거 수를 증가 또는 감소시킨다.(완료)
     * 9. 자전거가 없는 대여소에서 상차를 하거나, 트럭에 자전거가 없는데 하차를 하려는 명령은 무시된다. 이때 명령은 무시되지만 시간은 정상적으로 각기 6초가 소비된다.
     * 10. 명령이 상, 하, 좌, 우 이동일 경우 트럭의 이동 거리를 100m 증가시키고, 트럭의 위치를 기록한다.(완료)
     * 11. 서비스 지역을 벗어나는 이동 명령은 무시된다. 이때 명령은 무시되지만 시간은 정상적으로 각기 6초가 소비된다.
     * 12. 매개 변수로 받은 명령 중 1분 내에 할 수 있는 명령까지만 수행한다. 즉, 한 번의 simulate 요청 안에서 각 트럭에게 내릴 수 있는 명령 수는 최대 10개이고, 그 이상의 명령은 실행되지 않는다.
     *     * Simulate 요청이 성공하면 카카오 T 바이크 서버의 상태가 ready 로 변경되고, 현재 시각이 1분 증가한다.
     * 13. 한 시나리오에서 Simulate 요청이 720번 성공되면 카카오 T 바이크 서버의 상태는 ready가 아닌 finished로 변경되며, 총점이 반영된다.
     * 14. 721번 이상도 Simulate 요청을 보낼 순 있으나, 이때의 이동거리 등은 점수에 반영되지 않는다.
     * 15. 다시 같은 시나리오를 Simulate 하고 싶다면 Start API를 이용해 새로운 AUTH_KEY를 발급받아야 한다.
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
        서버의 처리 순서
        서버는 작업을 다음 순으로 처리한다.
        정시 분(minute)마다 사용자가 대여 요청을 할 때 받은 정보를 이용해, 사용 시간이 다 된 자전거를 반납 처리한다.
        정시 분(minute)마다 사용자가 보낸 새로운 대여 요청을 순서대로 처리한다.
        예를 들어 자전거가 1대 있는 자전거 대여소에 대여 요청이 2개 들어온 경우 json array(하단의 과거 대여 요청 기록 참고)의
        앞쪽에 위치한 대여 요청에게 자전거를 빌려주고, 뒤에 있는 대여 요청은 취소한다.
        죠르디의 지시에 따라 트럭을 운행한다.
     */
    @PostMapping("/rent")
    public RentResponseDTO rent(@RequestBody Map<String, List> reqDto, HttpServletRequest request, HttpServletResponse response) {

        return bikeSystemService.rent(reqDto);

    }

}
