# Next Route Library 0.0.1 (MVC)
편합니다!


##GET
pom.xml에 아래의 레파지토리와 Dependency설정을 추가합니다.

###Repository
    <repository>
        <id>next-mvn-repo</id>
        <url>https://raw.github.com/zerohouse/next/mvn-repo/</url>
    </repository>

###Dependency
	<dependency>
		<groupId>at.begin</groupId>
		<artifactId>next-route</artifactId>
		<version>0.0.1</version>
	</dependency>


#MVC

## Example Usage

    @Router
    @When("/api/user")
    @Before("loginCheck")
	public class UserController {
		@Build
		GDAO<User> userDAO;
	
		@Build(ImplementedBy=DeleteRight.class)
		Right right;
		
		@When(value = "/login", method = Methods.POST)
        @Before("!loginCheck")
		public Response login(@JsonParameter("user") User user, HttpSession session) {
			User fromDB = userDao.find(user.getEmail());
			if (fromDB == null)
				return new Json(true, "가입하지 않은 이메일입니다.", null);
			if (!fromDB.getPassword().equals(user.getPassword()))
				return new Json(true, "비밀번호가 다릅니다.", null);
			session.setAttribute("user", fromDB);
			return new Json(fromDB);
		}
	}
    
## Method Return Type별 응답
#### 1. Response.class Interface
    new Json(JsonObject);
    new Jsp(Jsp파일명);
    new StaticFile(파일명); // webapp/파일
    new Plain(스트링); //스트링 리턴
    
#### 2-1. String이 forward:로 시작할때 :뒤의 Path로 forward(클라이언트에서 주소 바뀌지 않음)
#### 2-2. String이 redirect:로 시작할때, :뒤의 Path로 reidrect(클라이언트 주소 바뀜) 
#### 2-3. String이 error:(no):(message)로 시작할때, :뒤의 메시지로 에러를 리턴
	ex)
	return "forward:/index.html" // forward to : /index.html 
	return "redirect:/index.html" // redirect to : /index.html 
	return "error:404" // 404에러 리턴
	return "error:404:페이지가 없습니다." // 404에러 리턴 + 에러메시지 리턴
#### 4. Object Return시 @Router의 defaultFactory에서 Response를 생성해 응답함.




## @Annotations
#### @Router [클래스 레벨]
라우터 클래스에 선언

    Class<? extends ResponseFactory> defaultFactory() default JsonFactory.class
    //해당 라우터에서 Object리턴시 지정된 Factory에서 Response생성후 응답    

####cf. JsonFactory
    public class JsonFactory implements ResponseFactory {
        @Override
    	public Response getResponse(Object returned) {
    		return new Json(returned);
    	}
    }


#### @When [클래스, 메서드 레벨]
Url 매핑 정보를 정의

    String[] value() default ""; // 매핑될 url들 ({}, *)
    String[] method() default "GET"; // 매핑될 메서드(Post, Get, Put, Delete등) 
	
### Uri 변수의 사용
모든 파라미터를 받을때 {}와 \*를 사용합니다.

\*와 {}의 차이점은 변수를 꺼낼 수 있느냐의 여부입니다.

	@Mapping("/{valueName}/*");
	http.getUriValue("valueName");
    @UriValue("valueName") String string;


#### @HttpMethod [메서드 레벨]
공통적으로 사용할 메서드 정의 @Before, @After에서 사용

    String value() default ""; // 매핑될 이름 값이 없으면 메서드 이름으로 매핑
    
#### @StringParameter, @FileParameter, @JsonParameter, @SessionAttribute, @Stored [파라미터 레벨]

    @Before("loginCheck")
    @When(value = "/update", method = Method.POST)
    public void updatePost(@StringParameter("userId") String parameter,
              @JsonParameter("Post") Post post,
              @SessionAttribute("user") User user, @Stored List<String> mylist) {
              //Stored의 경우
              //Store store를 꺼내 저장한 속성을 뺄 수 있음.
    }
    
#### 임의의 파라미터를 inject 하고자 할 경우 파라미터 Inject를 임플리먼트한 클래스를 생성
    @ParameterInject
    public class HttpInject implements Inject {
    
        @Override
    	public Object getParameter(Http http, Store store, Class<?> type, Parameter obj) {
    		return http;
    	}
    
    	@Override
    	public boolean matches(Class<?> type, Parameter obj) {
    		return type.equals(Http.class);
    	}
    
    }



### UploadFile.class
쉬운 파일 업로드. 파라미터에서 받고, 파일이름 바꾸고, 세이브.

#### File Upload Example
    
    
    @Router
    @When(value = "/api/upload")
    public class UploadController {
    
        @Bind
    	DAO dao;
    
    	@When(value = "/profilePhoto", method = Methods.POST)
    	public Object profile(@SessionAttribute("user") User user,
            @FileParameter("photo") UploadFile file) throws IOException {
    		file.setFileName("profile_" + user.getEmail().replace('@', '_'));
    		user.setPhotoUrl(file.getUriPath());
    		file.save();
    		dao.update(user);
    		return user;
    	}
    
    }



# Setting
1. 아래 web.xml을 webapp디렉토리의 WEB-INF폴더 내에 위치.
2. resource폴더 내에 next.json 위치 (기본 세팅을 담당)

## web.xml (webapp/WEB-INF/web.xml)
    <?xml version="1.0" encoding="UTF-8"?>
    <web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns="http://java.sun.com/xml/ns/javaee"
    	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
    	http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
    	version="3.0">
    	<listener>
    		<listener-class>next.route.Next</listener-class>
    	</listener>
    </web-app>


## next-route.json (resources/next-route.json)
### Setting Example
	{
	  "basePackage": "",
	  "mappings": [
	    "/",
        "!.css",
        "!.html",
        "!.js"
	  ],
	  "url": "localhost:8080",
	  "jspPath": "/WEB-INF/jsp/"
	}



### Default Setting : 기본 세팅은 아래와 같습니다.
	{
	  "basePackage": "",
	  "mappings": [],
	  "characterEncoding": "UTF-8",
	  "url": "",
	  "jspPath": "",
	  "dateFormat": "yyyy-MM-dd HH:mm:ss",
	  "upload": {
	    "location": "uploads/",
	    "tempSaveLocation": "uploads/temp/",
	    "maxFileSize": 5242880, //5MB
	    "maxRequestSize": 26214400, //25MB
	    "fileSizeThreshold": 1048576 //1MB
	  }
	}


## cf. [bind를 통한 Dependency Inection](https://github.com/zerohouse/next-bind)
## cf. [Jdbc-mysql Library](https://github.com/zerohouse/next-jdbc-mysql)
