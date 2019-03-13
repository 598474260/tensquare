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
        // 设置头部信息
        builder.setHeader(map);

        // 设置载荷
        builder.setId(id);
        // 用户名
        builder.setSubject(subject);
        // 设置角色
        builder.claim("roles",roles);
        // 设置生成token的时间
        builder.setIssuedAt(new Date());
        // 获取当前时间
        long millis = System.currentTimeMillis();
        // 设置时间，设置一天时间
        // millis += 1000*60*60*24;
        // 设置过期时间
        millis += num;
        // 设置过期时间
        builder.setExpiration(new Date(millis));
        // 设置签名
        builder.signWith(SignatureAlgorithm.HS256,key);

        // 生成token，返回
        return builder.compact();
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