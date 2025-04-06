package dev.Gardensong.Gamedev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 이 컨트롤러는 React 같은 SPA(Single Page Application)를 사용할 떄
 *  브라우저의 경로(URL)를 직접 입력해도 React 앱이 정상적으로 동작하도록 도와주는 역할을 함.
 */
@Controller
public class ViewController {
/**
 * "/" 루트 경로부터 시작해서, "/chat","/about", "/chat/something" 등등
 * 다양한 경로로 들어오는 모든 요청을 일괄적으로 index.html로 보내주는 역할을 함.
 *.
 * 이 방식은 React-Router를 사용하는 SPA 에서 새로고침(F5)하거나 직접 경로 입력 시
 * 서버가 그 경로를 모를 때 "404 에러"를 방지해줌.
 *.
 * React 앱이 로드된 후에는 클라이언트에서 라우팅을 처리하게 됨.
 */

    @RequestMapping(value = {
            "/",                //루트 경로
            "/chat",            // /chat 같은 단일 경로
            "/{x:[\\w\\-]+}",   // /chat, /about, /game 등 알파벳/숫자/하이픈 포함
            "/**/{x:[\\w\\-]+}" // /chat/room, /game/dev 등 하위 경로까지 포함
    })
    public String forwardReactRoutes() {
        //index.html 로 포워딩 -> React 앱이 다시 로딩되어 클라이언트 라우팅 실행
        return "forward:/index.html";  // 모든 경로를 React로 포워딩
    }
}
