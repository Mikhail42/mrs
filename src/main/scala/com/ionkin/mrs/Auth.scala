package com.ionkin.mrs

import java.security.{AlgorithmParameters, SecureRandom}
import java.util.Base64

import com.ionkin.mrs.dao.PersonDao
import com.ionkin.mrs.model.User
import javax.crypto.{Cipher, SecretKeyFactory}
import javax.crypto.spec.{IvParameterSpec, PBEKeySpec, SecretKeySpec}

import scala.concurrent.Future

object Auth {
  import model.Data.executionContext

  private val charsetName = "UTF-8"
  private val secretKeySpecAlgotithm = "AES"
  private val cipherTransform = "AES/CBC/PKCS5Padding"
  private val secretKeyFactoryAlgorithm = "PBKDF2WithHmacSHA512"

  private val secretPassword = "sfds3XSJSc"
  private val iterationCount = 1000
  private val keyLength = 256

  def checkUser(login: String, clearPassword: String)(implicit personDao: PersonDao): Future[Boolean] = {
    val userFut: Future[Option[User]] = personDao.searchUser(u => u.login == login)
    userFut.map {
      case Some(user) => {
        val salt = base64Decode(user.salt)
        val key: SecretKeySpec = createSecretKey(secretPassword.toCharArray, salt, iterationCount, keyLength)
        clearPassword == decrypt(user.password, key)
      }
      case None => false
    }
  }

  def encrypt(password: String, saltBase64: String): String = {
    val key: SecretKeySpec = createSecretKey(secretPassword.toCharArray, base64Decode(saltBase64), iterationCount, keyLength)
    encrypt(password, key)
  }

  private def encrypt(value: String, key: SecretKeySpec): String = {
    val pbeCipher = Cipher.getInstance(cipherTransform)
    pbeCipher.init(Cipher.ENCRYPT_MODE, key)
    val parameters: AlgorithmParameters = pbeCipher.getParameters
    val ivParameterSpec: IvParameterSpec = parameters.getParameterSpec(classOf[IvParameterSpec])
    val cryptoText = pbeCipher.doFinal(value.getBytes(charsetName))
    base64Encode(ivParameterSpec.getIV) + ":" + base64Encode(cryptoText)
  }
  private def decrypt(value: String, key: SecretKeySpec): String = {
    val Array(iv, property)  = value.split(":")
    val pbeCipher = Cipher.getInstance(cipherTransform)
    pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)))
    new String(pbeCipher.doFinal(base64Decode(property)), charsetName)
  }
  private def createSecretKey(password: Array[Char], salt: Array[Byte], iterationCount: Int, keyLength: Int): SecretKeySpec = {
    val keyFactory = SecretKeyFactory.getInstance(secretKeyFactoryAlgorithm)
    val keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength)
    val keyTmp = keyFactory.generateSecret(keySpec)
    new SecretKeySpec(keyTmp.getEncoded, secretKeySpecAlgotithm)
  }
  private[mrs] def createSalt(): Array[Byte] = {
    // there are only one algorithm
    // https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#SecureRandom
    val sr = SecureRandom.getInstance("SHA1PRNG")
    val salt = new Array[Byte](16) // 16 * 8 == 128 bit
    sr.nextBytes(salt)
    salt
  }
  private[mrs] def createSaltAsBase64(): String = base64Encode(createSalt())

  private def base64Encode: Array[Byte] => String  = Base64.getEncoder.encodeToString
  private def base64Decode: String => Array[Byte] = Base64.getDecoder.decode
}
