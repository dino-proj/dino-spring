package cn.dinodev.spring.commons.crypto;

import java.math.BigInteger;

import org.bouncycastle.crypto.signers.DSAEncoding;
import org.bouncycastle.crypto.signers.StandardDSAEncoding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

/**
 * SM2数字签名结果类，包含签名的r和s值
 * 
 * @author Cody Lu
 * @since 2022-05-06
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sm2Signature {
  private BigInteger signatureR;
  private BigInteger signatureS;

  /**
   * 返回签名的字符串表示形式。
   * <p>
   * 将r和s值转换为16进制字符串，用逗号分隔。
   * </p>
   * 
   * @return 签名的16进制字符串表示，格式为"r,s"
   */
  @Override
  public String toString() {
    return signatureR.toString(16) + "," + signatureS.toString(16);
  }

  /**
   * 从标准DSA编码的字节数组创建SM2签名对象。
   * <p>
   * 使用默认的StandardDSAEncoding实例解码签名数据。
   * </p>
   * 
   * @param signDSAEncoding DSA编码的签名字节数组
   * @return SM2签名对象
   */
  public static Sm2Signature fromStandardDSA(byte[] signDSAEncoding) {
    return fromStandardDSA(StandardDSAEncoding.INSTANCE, signDSAEncoding);
  }

  /**
   * 从指定DSA编码器和字节数组创建SM2签名对象。
   * <p>
   * 使用指定的DSA编码器解码签名数据，提取r和s值。
   * </p>
   * 
   * @param dsaEncoding DSA编码器实例
   * @param signDSAEncoding DSA编码的签名字节数组
   * @return SM2签名对象
   */
  @SneakyThrows
  public static Sm2Signature fromStandardDSA(DSAEncoding dsaEncoding, byte[] signDSAEncoding) {

    BigInteger[] decodedValues = dsaEncoding.decode(Sm2.SM2_ECC_N, signDSAEncoding);
    var signatureRValue = decodedValues[0];
    var signatureSValue = decodedValues[1];
    return new Sm2Signature(signatureRValue, signatureSValue);
  }
}