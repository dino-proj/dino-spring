package cn.dinodev.spring.commons.crypto;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECFieldFp;
import java.security.spec.EllipticCurve;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithID;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.crypto.signers.StandardDSAEncoding;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.gm.SM2P256V1Curve;

import lombok.experimental.UtilityClass;

/**
 * SM2椭圆曲线加密算法工具类
 * <p>提供SM2密钥生成、加密、解密等功能的静态方法</p>
 *
 * @author Cody Lu
 * @since 2022-05-06
 */

@UtilityClass
public class Sm2 {

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  public static final SM2P256V1Curve CURVE = new SM2P256V1Curve();
  public final static BigInteger SM2_ECC_P = CURVE.getQ();
  public final static BigInteger SM2_ECC_A = CURVE.getA().toBigInteger();
  public final static BigInteger SM2_ECC_B = CURVE.getB().toBigInteger();
  public final static BigInteger SM2_ECC_N = CURVE.getOrder();
  public final static BigInteger SM2_ECC_H = CURVE.getCofactor();
  public final static BigInteger SM2_ECC_GX = new BigInteger(
      "32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16);
  public final static BigInteger SM2_ECC_GY = new BigInteger(
      "BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16);
  public static final ECPoint G_POINT = CURVE.createPoint(SM2_ECC_GX, SM2_ECC_GY);
  public static final ECDomainParameters SM2_DOMAIN = new ECDomainParameters(CURVE, G_POINT,
      SM2_ECC_N, SM2_ECC_H);
  public static final int CURVE_LEN = getCurveLength(SM2_DOMAIN);

  public static final EllipticCurve JDK_CURVE = new EllipticCurve(new ECFieldFp(SM2_ECC_P), SM2_ECC_A, SM2_ECC_B);
  public static final java.security.spec.ECPoint JDK_G_POINT = new java.security.spec.ECPoint(
      G_POINT.getAffineXCoord().toBigInteger(), G_POINT.getAffineYCoord().toBigInteger());
  public static final java.security.spec.ECParameterSpec JDK_EC_SPEC = new java.security.spec.ECParameterSpec(
      JDK_CURVE, JDK_G_POINT, SM2_ECC_N, SM2_ECC_H.intValue());

  /**
   * 计算椭圆曲线的长度。
   * <p>
   * 根据椭圆曲线域参数计算曲线长度，用于确定密钥和坐标的字节长度。
   * </p>
   *
   * @param domainParams 椭圆曲线域参数
   * @return 曲线长度（字节数）
   */
  public static int getCurveLength(ECDomainParameters domainParams) {
    return (domainParams.getCurve().getFieldSize() + 7) / 8;
  }

  /**
   * 生成SM2密钥对。
   * <p>
   * 使用安全随机数生成器创建一对SM2椭圆曲线密钥，包含公钥和私钥。
   * </p>
   *
   * @return 包含公钥和私钥字节数组的密钥对
   */
  public static KeyPair generateKeyPair() {
    SecureRandom random = new SecureRandom();
    ECKeyGenerationParameters keyGenerationParams = new ECKeyGenerationParameters(SM2_DOMAIN, random);
    ECKeyPairGenerator keyGen = new ECKeyPairGenerator();
    keyGen.init(keyGenerationParams);
    AsymmetricCipherKeyPair keyPair = keyGen.generateKeyPair();
    ECPublicKeyParameters ecPublicKeyParameters = (ECPublicKeyParameters) keyPair.getPublic();
    ECPrivateKeyParameters ecPrivateKeyParameters = (ECPrivateKeyParameters) keyPair.getPrivate();

    return new KeyPair(getRawPrivateKey(ecPrivateKeyParameters), getRawPublicKey(ecPublicKeyParameters));
  }

  /**
   * 构建椭圆曲线公钥参数。
   * <p>
   * 根据64字节的公钥数据（前32字节为x坐标，后32字节为y坐标）构建ECPublicKeyParameters对象。
   * </p>
   *
   * @param publicKey 64字节的公钥数据
   * @return 椭圆曲线公钥参数对象
   */
  public static ECPublicKeyParameters buildECPublicKeyParameters(byte[] publicKey) {
    ECPoint pointQ = CURVE.createPoint(new BigInteger(1, publicKey, 0, 32), new BigInteger(1, publicKey, 32, 32));
    return new ECPublicKeyParameters(pointQ, SM2_DOMAIN);
  }

  /**
   * 构建椭圆曲线私钥参数。
   * <p>
   * 根据私钥字节数组构建ECPrivateKeyParameters对象。
   * </p>
   *
   * @param privateKey 私钥字节数组
   * @return 椭圆曲线私钥参数对象
   */
  public static ECPrivateKeyParameters buildECPrivateKeyParameters(byte[] privateKey) {
    BigInteger privateKeyValue = new BigInteger(1, privateKey);
    return new ECPrivateKeyParameters(privateKeyValue, SM2_DOMAIN);
  }

  /**
   * 提取私钥中的d值。
   * <p>
   * 从ECPrivateKeyParameters对象中提取私钥的d值，并转换为固定长度的字节数组。
   * </p>
   *
   * @param privateKey 椭圆曲线私钥参数对象
   * @return 曲线长度的私钥字节数组
   */
  public static byte[] getRawPrivateKey(ECPrivateKeyParameters privateKey) {
    return toCurveLengthBytes(privateKey.getD().toByteArray());
  }

  /**
   * 提取公钥中的XY坐标分量。
   * <p>
   * 从ECPublicKeyParameters对象中提取公钥的x和y坐标，组合成64字节的数组。
   * </p>
   *
   * @param publicKey 椭圆曲线公钥参数对象
   * @return 2倍曲线长度的公钥字节数组（x坐标+y坐标）
   */
  public static byte[] getRawPublicKey(ECPublicKeyParameters publicKey) {
    byte[] src65 = publicKey.getQ().getEncoded(false);
    byte[] rawXY = new byte[CURVE_LEN * 2];
    System.arraycopy(src65, 1, rawXY, 0, rawXY.length);
    return rawXY;
  }

  /**
   * 使用公钥加密数据。
   * <p>
   * 使用指定的公钥参数和加密模式对输入数据进行SM2加密。
   * </p>
   *
   * @param input 待加密的数据
   * @param ecPublicKeyParameters 椭圆曲线公钥参数
   * @param mode SM2加密模式（C1C2C3或C1C3C2）
   * @return 加密后的数据
   * @throws InvalidCipherTextException 如果加密过程中发生错误
   */
  public static byte[] encrypt(byte[] input, ECPublicKeyParameters ecPublicKeyParameters, SM2Engine.Mode mode)
      throws InvalidCipherTextException {
    SM2Engine engine = new SM2Engine(mode);
    ParametersWithRandom parametersWithRandom = new ParametersWithRandom(ecPublicKeyParameters, new SecureRandom());
    engine.init(true, parametersWithRandom);
    return engine.processBlock(input, 0, input.length);
  }

  /**
   * 公钥加密
   * @param input 待加密数据
   * @param publicKey 公钥参数
   * @param mode 加密方式
   * @return
   * @throws InvalidCipherTextException
   */
  public static byte[] encrypt(byte[] input, byte[] publicKey, SM2Engine.Mode mode) throws InvalidCipherTextException {
    var ecPublicKeyParameters = buildECPublicKeyParameters(publicKey);

    return encrypt(input, ecPublicKeyParameters, mode);
  }

  /**
   * 使用字节数组公钥加密数据（默认C1C2C3模式）。
   * <p>
   * 将64字节的公钥字节数组转换为椭圆曲线公钥参数，然后使用SM2Engine.Mode.C1C2C3模式进行加密。
   * </p>
   *
   * @param input 待加密的数据
   * @param publicKey 64字节的公钥字节数组（x坐标+y坐标）
   * @return 加密后的数据
   * @throws InvalidCipherTextException 如果加密过程中发生错误
   */
  public static byte[] encrypt(byte[] input, byte[] publicKey) throws InvalidCipherTextException {
    var ecPublicKeyParameters = buildECPublicKeyParameters(publicKey);

    return encrypt(input, ecPublicKeyParameters, SM2Engine.Mode.C1C2C3);
  }

  /**
   * 使用私钥解密数据。
   * <p>
   * 使用指定的私钥参数和解密模式对输入数据进行SM2解密。
   * </p>
   *
   * @param input 待解密的数据
   * @param ecPrivateKeyParameters 椭圆曲线私钥参数
   * @param mode SM2解密模式（C1C2C3或C1C3C2）
   * @return 解密后的数据
   * @throws InvalidCipherTextException 如果解密过程中发生错误
   */
  public static byte[] decrypt(byte[] input, ECPrivateKeyParameters ecPrivateKeyParameters, SM2Engine.Mode mode)
      throws InvalidCipherTextException {
    SM2Engine engine = new SM2Engine(mode);
    engine.init(false, ecPrivateKeyParameters);
    return engine.processBlock(input, 0, input.length);
  }

  /**
   * 使用字节数组私钥解密数据。
   * <p>
   * 将32字节的私钥字节数组转换为椭圆曲线私钥参数，然后使用指定模式进行解密。
   * </p>
   *
   * @param input 待解密的数据
   * @param privateKey 32字节的私钥字节数组
   * @param mode SM2解密模式（C1C2C3或C1C3C2）
   * @return 解密后的数据
   * @throws InvalidCipherTextException 如果解密过程中发生错误
   */
  public static byte[] decrypt(byte[] input, byte[] privateKey, SM2Engine.Mode mode) throws InvalidCipherTextException {
    var ecPrivateKeyParameters = buildECPrivateKeyParameters(privateKey);
    return decrypt(input, ecPrivateKeyParameters, mode);
  }

  /**
   * 使用字节数组私钥解密数据（默认C1C2C3模式）。
   * <p>
   * 将32字节的私钥字节数组转换为椭圆曲线私钥参数，然后使用SM2Engine.Mode.C1C2C3模式进行解密。
   * </p>
   *
   * @param input 待解密的数据
   * @param privateKey 32字节的私钥字节数组
   * @return 解密后的数据
   * @throws InvalidCipherTextException 如果解密过程中发生错误
   */
  public static byte[] decrypt(byte[] input, byte[] privateKey) throws InvalidCipherTextException {
    var ecPrivateKeyParameters = buildECPrivateKeyParameters(privateKey);
    return decrypt(input, ecPrivateKeyParameters, SM2Engine.Mode.C1C2C3);
  }

  /**
   * 使用私钥对数据进行数字签名。
   * <p>
   * 使用指定的私钥参数和用户标识对输入数据进行SM2数字签名。
   * 如果用户标识为空或null，则不使用用户标识进行签名。
   * </p>
   *
   * @param input 待签名的数据
   * @param ecPrivateKeyParameters 椭圆曲线私钥参数
   * @param userId 用户标识字节数组，可以为null
   * @return Sm2Signature对象，包含r和s分量的签名结果
   * @throws CryptoException 如果签名过程中发生错误
   */
  public static Sm2Signature sign(byte[] input, ECPrivateKeyParameters ecPrivateKeyParameters, byte[] userId)
      throws CryptoException {
    SM2Signer signer = new SM2Signer();
    CipherParameters param;
    if (userId != null && userId.length > 0) {
      param = new ParametersWithID(ecPrivateKeyParameters, userId);
    } else {
      param = ecPrivateKeyParameters;
    }
    signer.init(true, param);
    signer.update(input, 0, input.length);
    byte[] sign = signer.generateSignature();

    return Sm2Signature.fromStandardDSA(sign);
  }

  /**
   * 使用字节数组私钥对数据进行数字签名。
   * <p>
   * 将32字节的私钥字节数组转换为椭圆曲线私钥参数，然后进行SM2数字签名。
   * </p>
   *
   * @param input 待签名的数据
   * @param privateKey 32字节的私钥字节数组
   * @param userId 用户标识字节数组，可以为null
   * @return Sm2Signature对象，包含r和s分量的签名结果
   * @throws CryptoException 如果签名过程中发生错误
   */
  public static Sm2Signature sign(byte[] input, byte[] privateKey, byte[] userId) throws CryptoException {
    var ecPrivateKeyParameters = buildECPrivateKeyParameters(privateKey);
    return sign(input, ecPrivateKeyParameters, userId);
  }

  /**
   * 使用公钥验证数字签名。
   * <p>
   * 使用指定的公钥参数和用户标识验证输入数据的SM2数字签名。
   * 如果用户标识为空或null，则不使用用户标识进行验证。
   * </p>
   *
   * @param input 原始数据
   * @param signature Sm2Signature签名对象，包含r和s分量
   * @param ecPublicKeyParameters 椭圆曲线公钥参数
   * @param userId 用户标识字节数组，可以为null
   * @return 如果签名验证成功返回true，否则返回false
   * @throws IOException 如果验证过程中发生IO错误
   */
  public static boolean verifySign(byte[] input, Sm2Signature signature,
      ECPublicKeyParameters ecPublicKeyParameters, byte[] userId) throws IOException {
    BigInteger signR = signature.getSignatureR();
    BigInteger signS = signature.getSignatureS();
    byte[] sign = StandardDSAEncoding.INSTANCE.encode(SM2_ECC_N, signR, signS);

    SM2Signer signer = new SM2Signer();
    CipherParameters param;
    if (userId != null && userId.length > 0) {
      param = new ParametersWithID(ecPublicKeyParameters, userId);
    } else {
      param = ecPublicKeyParameters;
    }
    signer.init(false, param);
    signer.update(input, 0, input.length);
    return signer.verifySignature(sign);
  }

  /**
   * 使用字节数组公钥验证数字签名。
   * <p>
   * 将64字节的公钥字节数组转换为椭圆曲线公钥参数，然后验证输入数据的SM2数字签名。
   * </p>
   *
   * @param input 原始数据
   * @param signature Sm2Signature签名对象，包含r和s分量
   * @param publicKey 64字节的公钥字节数组（x坐标+y坐标）
   * @param userId 用户标识字节数组，可以为null
   * @return 如果签名验证成功返回true，否则返回false
   * @throws IOException 如果验证过程中发生IO错误
   */
  public static boolean verifySign(byte[] input, Sm2Signature signature,
      byte[] publicKey, byte[] userId) throws IOException {
    var ecPublicKeyParameters = buildECPublicKeyParameters(publicKey);
    return verifySign(input, signature, ecPublicKeyParameters, userId);
  }

  /**
   * 将字节数组转换为曲线长度的字节数组。
   * <p>
   * 如果输入数组长度等于曲线长度(32字节)，直接返回。
   * 如果长度大于曲线长度，截取末尾32字节。
   * 如果长度小于曲线长度，在前面补零至32字节。
   * </p>
   *
   * @param src 源字节数组
   * @return 长度为32字节的字节数组
   */
  private static byte[] toCurveLengthBytes(byte[] src) {
    if (src.length == CURVE_LEN) {
      return src;
    }

    byte[] result = new byte[CURVE_LEN];
    if (src.length > CURVE_LEN) {
      System.arraycopy(src, src.length - result.length, result, 0, result.length);
    } else {
      System.arraycopy(src, 0, result, result.length - src.length, src.length);
    }
    return result;
  }
}