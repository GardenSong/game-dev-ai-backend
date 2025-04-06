// 이 파일이 어떤 "패키지(폴더)" 안에 있는지 나타냄.
// 이 경로에 있는 다른 클래스들과 묶여 있음.
package dev.Gardensong.Gamedev;

//Spring Boot를 실행하기 위한 기본 도구를 가져오는 코드
import org.springframework.boot.SpringApplication; //Spring Boot 앱을 실행시켜주는 클래스
import org.springframework.boot.autoconfigure.SpringBootApplication; //자동으로 필요한 설정을 해주는 애너테이션

// 이 클래스가 Spring Boot 애플리케이션의 시작점이라 걸 알려주는 표시
// 내부적으로 @Configuration, @EnableAutoconFiguration, @ComponentScan 을 포함하고 있어.
@SpringBootApplication //이거를 통해서 먼저 ComponetScan이 되어서 그 하위 폴더 (Controller, service, repository)같은 것들을 스캔함.
public class GamedevApplication { //클래스 이름. 이 프로젝트의 중심이 되는 클래스야

//    이게 자바 프로그램의 시작 지점 (main 함수)
    public static void main(String[] args) {
//        Spring Boot 애플리케이션을 시작하라는 명령어야.
//        내부적으로 서버를 켜고, 필요한 설정을 불러오고, 준비된 controller/service 등을 실행 시켜줘
        SpringApplication.run(dev.Gardensong.Gamedev.GamedevApplication.class, args);
    }
}
