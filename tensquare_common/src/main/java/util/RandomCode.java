package util;

import org.springframework.stereotype.Component;

@Component
public class RandomCode {

	/**
	 * 生成验证码
	 * @return
	 */
	public String genCode(){
		// 199999  nums = [0,9]
		int code = (int)((Math.random()*9+1)*100000);
		return code+"";
	}

}
