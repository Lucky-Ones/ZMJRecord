package utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @outhor LinZeHang
 * @creat 2022-05-04-20:11
 */
public class MybatisUtils {
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            //ʹ��mybatis�ĵ�һ������ȡSqlSessionFactory���������仰�����ģ����Է�װ�ɹ�����
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //��ȡSqlSession����
    public static SqlSession getSession(){
        return sqlSessionFactory.openSession();
    }
}
