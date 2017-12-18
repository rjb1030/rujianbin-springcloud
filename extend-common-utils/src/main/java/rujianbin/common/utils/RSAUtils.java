
package rujianbin.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加解密
 */
public final class RSAUtils {

	private static final Logger		LOGGER		= LoggerFactory.getLogger(RSAUtils.class);

	/** 安全服务提供者 */
	private static final Provider	PROVIDER	= new BouncyCastleProvider();

	/** 密钥大小 */
	private static final int		KEY_SIZE	= 1024;

	//算法名称/加密模式/填充方式
	private static final String ALGORITHM = "RSA/ECB/PKCS1Padding";

	public static final String PRIVATE_KEY_SESSION_ATTRIBUTE_NAME = "session_privateKey";

	/**
	 * 不可实例化
	 */
	private RSAUtils() {
	}

	/**
	 * 生成密钥对
	 * 
	 * @return 密钥对
	 */
	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", PROVIDER);
			keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
			return keyPairGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 加密
	 * 
	 * @param publicKey
	 *            公钥
	 * @param data
	 *            数据
	 * @return 加密后的数据
	 */
	public static byte[] encrypt(PublicKey publicKey, byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 加密 (明文过长时分片加密)
	 * 
	 * @param publicKey
	 *            公钥
	 * @param text
	 *            字符串
	 * 
	 * @return Base64编码字符串
	 */
	public static String encrypt(PublicKey publicKey, String text) {
		byte[] data = crypt( text.getBytes(),true,publicKey);
		return data != null ? Base64.encodeBase64String(data) : null;
	}

	private static byte[] crypt(byte[] data,boolean encode,Key key){
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
			if(encode) {
				cipher.init(Cipher.ENCRYPT_MODE, key);
			}else{
				cipher.init(Cipher.DECRYPT_MODE, key);
			}
			int blockSize = cipher.getBlockSize();
			ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
			int j = 0;
			blockSize = data.length < blockSize ? data.length : blockSize;
			while (data.length - j * blockSize > 0) {
				if (j * blockSize + blockSize > data.length) {
					bout.write(cipher.doFinal(data, j * blockSize, data.length - j * blockSize));
				} else {
					bout.write(cipher.doFinal(data, j * blockSize, blockSize));
				}
				j++;
			}
			return bout.toByteArray();
		}catch (Exception e){
			LOGGER.error("",e);
		}
		return new byte[]{} ;
	}

	/**
	 * 解密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param data
	 *            数据
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(PrivateKey privateKey, byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 解密（分片解密）
	 * 
	 * @param privateKey
	 *            私钥
	 * @param text
	 *            Base64编码字符串
	 * @return 解密后的数据
	 */
	public static String decrypt(PrivateKey privateKey, String text) {
		byte[] data = crypt(Base64.decodeBase64(text),false,privateKey);
		return data != null ? new String(data) : null;
	}


	private static String getKeyStr(File file) throws IOException {

		try {
			BufferedReader bf=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			//最好在将字节流转换为字符流的时候 进行转码
			StringBuffer buffer=new StringBuffer();
			String line;
			while((line=bf.readLine())!=null){
				buffer.append(line);
			}

			return buffer.toString();
		} catch (IOException e) {
			throw e;
		}
	}

	public static void main(String[] args) throws Exception{

		//直接生成公钥和私钥文件
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String realPath = path.substring(0,path.indexOf("/target"));
		System.out.println(realPath);

		String prkPath = realPath+"/other/privateKey.ser";
		String pukPath = realPath+"/other/publicKey.ser";

//		//####################   生产公钥和私钥文件    ####################
//		KeyPair keyPair = 	RSAUtils.generateKeyPair();
//
//		RSAPublicKey publicKey  = (RSAPublicKey)keyPair.getPublic() ;
//		RSAPrivateKey privateKey =(RSAPrivateKey) keyPair.getPrivate() ;
//
//		FileOutputStream fs = new FileOutputStream(prkPath);
//		PrintStream ps = new PrintStream(fs);
//		ps.print(Base64.encodeBase64String(privateKey.getEncoded()));
//
//		FileOutputStream fs2 = new FileOutputStream(pukPath);
//		PrintStream ps2 = new PrintStream(fs2);
//		ps2.print(Base64.encodeBase64String(publicKey.getEncoded()));
//		//####################   生产公钥和私钥文件    ####################



		//####################  读取公钥和私钥文件    ####################
		String pukKey = getKeyStr(new File(pukPath));
		X509EncodedKeySpec x509 = new X509EncodedKeySpec(Base64.decodeBase64(pukKey));
		KeyFactory keyf = KeyFactory.getInstance("RSA");
		PublicKey temp_publicKey = keyf.generatePublic(x509);

		String prkKey = getKeyStr(new File(prkPath));
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(prkKey));
		PrivateKey temp_privateKey = keyf.generatePrivate(priPKCS8);
		//####################  读取公钥和私钥文件    ####################


		//####################  操作加解密    ####################
		String ori = "你好1234";
		String  encodeStr = RSAUtils.encrypt(temp_publicKey,ori);
		System.out.println(encodeStr);
		String ori2 = RSAUtils.decrypt(temp_privateKey, encodeStr);
		System.out.println(ori2);
		//####################  操作加解密    ####################

	}

}
