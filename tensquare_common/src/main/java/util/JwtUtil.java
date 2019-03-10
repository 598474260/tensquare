package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("jwt.config")
public class JwtUtil {

    // 秘钥，编写到配置文件中，通过读取配置文件获取到秘钥的值，提供get和set方法
    private String key;
    // 用户设置的token过期时间
    private long num;

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public long getNum() {
        return num;
    }
    public void setNum(long num) {
        this.num = num;
    }

    /**
     * 生成token
     * @return
     */
    public String createToken(String id,String subject,String roles){
        JwtBuilder builder = Jwts.builder();
        Map<String,Object> map = new HashMap<>();
        map.put("typ","JWT");
        // 设置头
        builder.setHeader(map);

        // 设置jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
        builder.setId(id);
        // 设置用户sub: jwt所面向的用户
        builder.setSubject(subject);
        // 设置签发时间 iat: jwt的签发时间
        builder.setIssuedAt(new Date());

        // 设置加密算法和秘钥(itcast) 需要严格保密
        builder.signWith(SignatureAlgorithm.HS256,key);

        // 获取到当前的时间
        long now = System.currentTimeMillis();
        // 添加数字，单位是毫秒，1000表示一秒
        // 设置一分钟
        now += num;

        // 设置过期时间
        builder.setExpiration(new Date(now));

        // 生成token
        String token = builder.compact();
        // 打印生成的token
        System.out.println(token);
        return token;
    }

    /**
     * 解析token
     * @return
     */
    public Claims parseToken(String token){
        // 解析，设置秘钥
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims;
    }
}