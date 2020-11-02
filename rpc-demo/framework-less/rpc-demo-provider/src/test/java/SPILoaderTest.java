import com.github.easyrpc.common.register.ProviderRegister;
import com.github.easyrpc.common.register.RegisterFactory;
import com.github.easyrpc.common.register.RegisterProperties;
import com.github.easyrpc.common.spi.SPILoader;
import org.junit.Test;

import java.util.List;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name SPILoaderTest
 * @Date 2020/10/30 9:41
 */
public class SPILoaderTest {

    @Test
    public void test0(){

        RegisterProperties properties = new RegisterProperties();
        properties.setHost("192.168.200.244");
        properties.setPort(6379);
        properties.setPassword("mypassword");

        List<RegisterFactory> objects = SPILoader.loadFactories(RegisterFactory.class, SPILoaderTest.class.getClassLoader());
        for (final RegisterFactory registerCenter : objects) {

            ProviderRegister instance = registerCenter.getInstance(properties);
            System.out.println(instance);

        }
    }

}
