package org.dinospring.commons.crypto;

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
/**
 *
 * @author tuuboo
 * @date 2022-05-06 06:40:57
 */

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
   * ???????????????????????????
   * @param domainParams
   * @return
   */
  public static int getCurveLength(ECDomainParameters domainParams) {
    return (domainParams.getCurve().getFieldSize() + 7) / 8;
  }

  /**
   * ???????????????
   *
   * @return
   *
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
   * ??????????????????
   * @param publicKey
   * @return
   */
  public static ECPublicKeyParameters buildECPublicKeyParameters(byte[] publicKey) {
    ECPoint pointQ = CURVE.createPoint(new BigInteger(1, publicKey, 0, 32), new BigInteger(1, publicKey, 32, 32));
    return new ECPublicKeyParameters(pointQ, SM2_DOMAIN);
  }

  /**
   * ??????????????????
   * @param privateKey
   * @return
   */
  public static ECPrivateKeyParameters buildECPrivateKeyParameters(byte[] privateKey) {
    BigInteger d = new BigInteger(1, privateKey);
    return new ECPrivateKeyParameters(d, SM2_DOMAIN);
  }

  /**
  * ???????????????d???
  *
  * @param privateKey
  * @return Curve?????????????????????
  */
  public static byte[] getRawPrivateKey(ECPrivateKeyParameters privateKey) {
    return toCurveLengthBytes(privateKey.getD().toByteArray());
  }

  /**
  * ???????????????XY??????
  *
  * @param publicKey
  * @return 2???Curve?????????????????????
  */
  public static byte[] getRawPublicKey(ECPublicKeyParameters publicKey) {
    byte[] src65 = publicKey.getQ().getEncoded(false);
    byte[] rawXY = new byte[CURVE_LEN * 2];
    System.arraycopy(src65, 1, rawXY, 0, rawXY.length);
    return rawXY;
  }

  /**
   * ????????????
   * @param input ???????????????
   * @param ecPublicKeyParameters ????????????
   * @param mode ????????????
   * @return
   * @throws InvalidCipherTextException
   */
  public static byte[] encrypt(byte[] input, ECPublicKeyParameters ecPublicKeyParameters, SM2Engine.Mode mode)
      throws InvalidCipherTextException {
    SM2Engine engine = new SM2Engine(mode);
    ParametersWithRandom parametersWithRandom = new ParametersWithRandom(ecPublicKeyParameters, new SecureRandom());
    engine.init(true, parametersWithRandom);
    return engine.processBlock(input, 0, input.length);
  }

  /**
   * ????????????
   * @param input ???????????????
   * @param publicKey ????????????
   * @param mode ????????????
   * @return
   * @throws InvalidCipherTextException
   */
  public static byte[] encrypt(byte[] input, byte[] publicKey, SM2Engine.Mode mode) throws InvalidCipherTextException {
    var ecPublicKeyParameters = buildECPublicKeyParameters(publicKey);

    return encrypt(input, ecPublicKeyParameters, mode);
  }

  /**
   * ????????????, ????????????SM2Engine.Mode.C1C2C3
   * @param input ???????????????
   * @param publicKey ????????????
   * @return
   * @throws InvalidCipherTextException
   */
  public static byte[] encrypt(byte[] input, byte[] publicKey) throws InvalidCipherTextException {
    var ecPublicKeyParameters = buildECPublicKeyParameters(publicKey);

    return encrypt(input, ecPublicKeyParameters, SM2Engine.Mode.C1C2C3);
  }

  /**
   * ????????????
   * @param input ???????????????
   * @param ecPrivateKeyParameters ????????????
   * @param mode ????????????
   * @return
   * @throws InvalidCipherTextException
   */
  public static byte[] decrypt(byte[] input, ECPrivateKeyParameters ecPrivateKeyParameters, SM2Engine.Mode mode)
      throws InvalidCipherTextException {
    SM2Engine engine = new SM2Engine(mode);
    engine.init(false, ecPrivateKeyParameters);
    return engine.processBlock(input, 0, input.length);
  }

  /**
   * ????????????
   * @param input ???????????????
   * @param privateKey ????????????
   * @param mode ????????????
   * @return
   * @throws InvalidCipherTextException
   */
  public static byte[] decrypt(byte[] input, byte[] privateKey, SM2Engine.Mode mode) throws InvalidCipherTextException {
    var ecPrivateKeyParameters = buildECPrivateKeyParameters(privateKey);
    return decrypt(input, ecPrivateKeyParameters, mode);
  }

  /**
   * ????????????, ????????????SM2Engine.Mode.C1C2C3
   * @param input ???????????????
   * @param privateKey ????????????
   * @return
   * @throws InvalidCipherTextException
   */
  public static byte[] decrypt(byte[] input, byte[] privateKey) throws InvalidCipherTextException {
    var ecPrivateKeyParameters = buildECPrivateKeyParameters(privateKey);
    return decrypt(input, ecPrivateKeyParameters, SM2Engine.Mode.C1C2C3);
  }

  /**
   * ????????????
   * @param input ???????????????
   * @param ecPrivateKeyParameters ????????????
   * @param ID ????????????
   * @return
   * @throws CryptoException
   */
  public static Sm2Signature sign(byte[] input, ECPrivateKeyParameters ecPrivateKeyParameters, byte[] ID)
      throws CryptoException {
    SM2Signer signer = new SM2Signer();
    CipherParameters param;
    if (ID != null && ID.length > 0) {
      param = new ParametersWithID(ecPrivateKeyParameters, ID);
    } else {
      param = ecPrivateKeyParameters;
    }
    signer.init(true, param);
    signer.update(input, 0, input.length);
    byte[] sign = signer.generateSignature();

    return Sm2Signature.fromStandardDSA(sign);
  }

  /**
   * ????????????
   * @param input ???????????????
   * @param privateKey ????????????
   * @param ID ????????????
   * @return
   * @throws CryptoException
   */
  public static Sm2Signature sign(byte[] input, byte[] privateKey, byte[] ID) throws CryptoException {
    var ecPrivateKeyParameters = buildECPrivateKeyParameters(privateKey);
    return sign(input, ecPrivateKeyParameters, ID);
  }

  /**
   * ??????????????????
   * @param input ????????????
   * @param SM2SignResult ??????
   * @param ecPublicKeyParameters ????????????
   * @param ID ????????????
   * @return
   * @throws IOException
   */
  public static boolean verifySign(byte[] input, Sm2Signature signature,
      ECPublicKeyParameters ecPublicKeyParameters, byte[] ID) throws IOException {
    BigInteger signR = signature.getR();
    BigInteger signS = signature.getS();
    byte[] sign = StandardDSAEncoding.INSTANCE.encode(SM2_ECC_N, signR, signS);

    SM2Signer signer = new SM2Signer();
    CipherParameters param;
    if (ID != null && ID.length > 0) {
      param = new ParametersWithID(ecPublicKeyParameters, ID);
    } else {
      param = ecPublicKeyParameters;
    }
    signer.init(false, param);
    signer.update(input, 0, input.length);
    return signer.verifySignature(sign);
  }

  /**
   * ??????????????????
   * @param input ????????????
   * @param SM2SignResult ??????
   * @param publicKey ????????????
   * @param ID ????????????
   * @return
   * @throws IOException
   */
  public static boolean verifySign(byte[] input, Sm2Signature signature,
      byte[] publicKey, byte[] ID) throws IOException {
    var ecPublicKeyParameters = buildECPublicKeyParameters(publicKey);
    return verifySign(input, signature, ecPublicKeyParameters, ID);
  }

  /**
   * ??????Curve?????????byte??????
   *
   * @param src
   * @return
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