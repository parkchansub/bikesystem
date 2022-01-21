package com.example.bikesystem.api.controller;

import com.example.bikesystem.dto.LocationsResponseDTO;
import com.example.bikesystem.dto.RentBikeRequestDTO;
import com.example.bikesystem.dto.SystemStartResponseDTO;
import com.example.bikesystem.item.Bike;
import com.example.bikesystem.service.BikeSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * 10시 00분: [[3, 0, 10]]
 * 10시 01분: [[1, 3, 1], [1, 4, 15]]
 * 10시 02분: [[0, 3, 2], [3, 1, 4], [0, 3, 1]]
 * 10시 03분: [[1, 3, 5]]
 * 각 원소는 각 사용자가 보낸 요청으로,
 * [자전거를 대여할 자전거 대여소 ID, 자전거를 반납할 자전거 대여소 ID, 자전거를 탈 시간(분 단위)]를 뜻한다.
 * 예를 들어 [3, 0, 10]은 ID가 3인 자전거 대여소에서 자전거를 대여하여, 10분 동안 자전거를 탄 후 ID가 0인 자전거 대여소에 자전거를 보관하겠다는 의미이다.
 *
 * */


/**
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
 * Start API
 * 문제를 풀기 위한 key를 발급한다. Start API를 실행하면 파라미터로 전달한 문제 번호에 맞게 각 자전거 대여소 및 트럭에 대한 정보를 초기화한다.
 *
 *
 *
 * Locations API
 * 현재 카카오 T 바이크 서비스 시각에 각 자전거 대여소가 보유한 자전거 수를 반환한다.
 *
 * Request
 * GET /locations
 * Authorization: {auth_key}
 * Content-Type: application/json
 * Header
 *
 * Name	Description
 * Authorization	Start API 에서 발급받은 auth_key
 * Example
 *
 * curl -X GET {BASE_URL}/locations \
 *      -H 'Authorization: {AUTH_KEY}' \
 *      -H 'Content-Type: application/json'
 * Response
 * Key
 *
 * Key	Type	Description
 * locations	Array of Location	각 자전거 대여소의 ID, 보유하고 있는 자전거 수에 대한 정보를 담은 배열
 * Example
 *
 * {
 *   "locations": [
 *     { "id": 0, "located_bikes_count": 3 },
 *     { "id": 1, "located_bikes_count": 3 },
 *     ...
 *   ]
 * }
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
     *
     * Name	Description
     * X-Auth-Token	문제에서 발급되는 응시자 식별 토큰 값
     * Parameter
     *
     * Name	Type	Description
     * problem	Integer	시나리오 번호 (1 <= problem <= 2)
     * Example
     *
     * curl -X POST {BASE_URL}/start \
     *      -H 'X-Auth-Token: {X_AUTH_TOKEN}' \
     *      -H 'Content-Type: application/json' \
     *      -d '{
     *          "problem": 1
     *      }'
     * Response
     * Key
     *
     * Key	Type	Description
     * auth_key	String	Start API 를 통해 발급받은 key, 이후 문제 풀이에 진행되는 모든 API에 이 key를 사용
     * problem	Integer	선택한 시나리오 번호
     * time	Integer	현재 카카오 T 바이크 서비스에서의 시각 (0부터 시작)
     * Example
     *
     * {
     *   "auth_key": "1fd74321-d314-4885-b5ae-3e72126e75cc",
     *   "problem": 1,
     *   "time": 0
     * }
     * @param reqDto
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/start")
    public SystemStartResponseDTO startBikeSystem(@RequestBody Map<String,String> reqDto, HttpServletRequest request, HttpServletResponse response){
        String problem = reqDto.get("problem");
        return bikeSystemService.startBikeSystem(problem);
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
    public LocationsResponseDTO getLocationInfo(@RequestBody Map<String,String> reqDto, HttpServletRequest request, HttpServletResponse response){
        String authorization = reqDto.get("Authorization");

        LocationsResponseDTO locationInfo = bikeSystemService.getLocationInfo(authorization);

        return locationInfo;
    }


    @GetMapping("/rentbike")
    public Bike requestRentBike(@RequestBody RentBikeRequestDTO rentBikeRequestDTO, HttpServletRequest request, HttpServletResponse response){
        return bikeSystemService.rentBike(rentBikeRequestDTO);
    }

}
