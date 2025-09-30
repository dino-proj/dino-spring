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
  private BigInteger r;
  private BigInteger s;

  public String toString() {
    return r.toString(16) + "," + s.toString(16);
  }

  public static Sm2Signature fromStandardDSA(byte[] signDSAEncoding) {
    return fromStandardDSA(StandardDSAEncoding.INSTANCE, signDSAEncoding);
  }

  @SneakyThrows
  public static Sm2Signature fromStandardDSA(DSAEncoding dsaEncoding, byte[] signDSAEncoding) {

    BigInteger[] bigIntegers = dsaEncoding.decode(Sm2.SM2_ECC_N, signDSAEncoding);
    var signR = bigIntegers[0];
    var signS = bigIntegers[1];
    return new Sm2Signature(signR, signS);
  }
}