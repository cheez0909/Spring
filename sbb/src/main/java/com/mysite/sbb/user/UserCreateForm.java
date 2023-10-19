package com.mysite.sbb.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
	
	@Size(min = 3, max = 25) // 이름이 3-25 글자 사이
	@NotEmpty(message = "사용자ID는 필수항목 입니다.")
	private String username;
	
	@NotEmpty(message = "비밀번호는 필수항목입니다.") // 검증 실패시 메세지
	private String password1;
	
	@NotEmpty(message = "비밀번호 확인는 필수항목입니다.")
	private String password2;
	
	@NotEmpty(message = "email은 필수항목입니다.")
	@Email // 속성값이 이메일 형식과 일치하는 지 여부
	private String email;
	
}
