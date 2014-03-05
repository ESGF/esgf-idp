package sec;

import java.io.*;

import java.math.BigInteger;

//import java.net.MalformedURLException;

import java.net.URL;
import java.net.URLConnection;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;

//import java.security.cert.X509Extension;

import java.util.*;

import javax.crypto.BadPaddingException;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;

import org.apache.http.HttpRequest;




//import org.apache.commons.codec.binary.Base64;


//import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.*;

import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;

import org.bouncycastle.asn1.x509.*;

import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.openssl.PasswordFinder;



import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;

import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.jce.X509Principal;

//import org.bouncycastle.jce.X509Principal;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.encoders.Base64;

import org.bouncycastle.x509.X509V2CRLGenerator;

import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;

public class SecurityCommons_esgf{
  public static final String CONTRAIL_ATTRIBUTE_ASSERTION = "1.34.5.0.14." + "67.101";
  public static final String CONTRAIL_ACCESS_TOKEN_OID = "1.34.5.0.14." + "67.102";

  public SecurityCommons_esgf() {
    
  }

  public String[] findRDNs(final String subject, ASN1ObjectIdentifier... oids) {

    String[] rdn = null;

    for (ASN1ObjectIdentifier oid : oids) {

      String rdnArr[] = getRDNs(subject, oid);
      if (rdnArr != null) {
        rdn = rdnArr;
        break;
      }

    }

    return rdn;

  }

  public boolean authorise(X509Certificate[] certs, final String allowedCNs) {

    boolean authorised = false;

    if (allowedCNs == null || allowedCNs.length() == 0) {
      authorised = true;

    } else {

      final String certSubject = certs[0].getSubjectDN().getName();

      String[] commonNames = getRDNs(certSubject, BCStyle.CN);

      if (commonNames != null && commonNames.length > 0) {

        String commonName = commonNames[0];
        if (allowedCNs.indexOf(commonName) > -1) {
          authorised = true;
        }

      }


    }
    return authorised;

  }

  public  boolean authorisedCN(final String allowedCNs, final String trialHostname) {

    boolean authorised = false;

    if (allowedCNs.indexOf(trialHostname) > -1) {
      authorised = true;
    }

    return authorised;

  }

  public boolean isValidFQDN(final String fqdn) {
    return true;
  }

  public boolean isUUID(final String uuid) {

    boolean isUUID = false;

    try {

      UUID.fromString(uuid);
      isUUID = true;

    } catch (IllegalArgumentException ex2) {
      ;

    }
    return isUUID;
  }

  public boolean isUserId(final String userId) {

    boolean isUserId = false;

    /*
     * A leading minus sign '-' would parse as an Integer,
     * but shouldn't be allowed as a UserID
     */

    if (!userId.startsWith("-")) {

      try {

        Integer.parseInt(userId);
        isUserId = true;

      } catch (NumberFormatException ex) {
        ;
      }


    }
    return isUserId;
  }

  public String[] getRDNs(final String x500Name, final ASN1ObjectIdentifier style) {

    ArrayList<String> al = new ArrayList<String>();

    X500Name x500name = new X500Name(x500Name.toString());

    RDN[] rdns = x500name.getRDNs(style);

    for (RDN rdn : rdns) {
      al.add(IETFUtils.valueToString(rdn.getFirst().getValue()));
    }

    String[] rdnsStr = null;

    if (al.size() > 0) {
      rdnsStr = new String[al.size()];
      al.toArray(rdnsStr);
    }

    return rdnsStr;
  }

  public ContentSigner getContentSigner(
    final PrivateKey privateKey,
    final String signatureAlgorithm // NOPMD
    )
    throws OperatorCreationException {

    final JcaContentSignerBuilder sigBuilder =
      new JcaContentSignerBuilder(signatureAlgorithm).setProvider(BouncyCastleProvider.PROVIDER_NAME);

    return sigBuilder.build(privateKey);

  }

  public void addKeyIDExtensions(
    final X509v3CertificateBuilder builder,
    final PublicKey authKey,
    final PublicKey subjectKey)
    throws InvalidKeyException {

    builder.addExtension(X509Extension.authorityKeyIdentifier, false,
      new AuthorityKeyIdentifierStructure(authKey));

    builder.addExtension(X509Extension.subjectKeyIdentifier, false,
      new SubjectKeyIdentifierStructure(subjectKey));

  }

  public X509Certificate createCertificate(
    final PublicKey subjectPublicKey,
    final String subjectName,
    final X509Certificate signingCertificate, //NOPMD
    final String signatureAlgorithm, //NOPMD
    final boolean isCA,
    final int days, final int hours, final int minutes)
    throws
    CertificateException,
    InvalidKeyException,
    OperatorCreationException {

    X509Certificate cert = null;

    return cert;

  }

  public X509CRL createCRL(
    X509Certificate caCert,
    PrivateKey caKey,
    BigInteger revokedSerialNumber,
    int reason)
    throws Exception {
    X509V2CRLGenerator crlGen = new X509V2CRLGenerator();
    Date now = new Date();

    final X500Principal issuerPrincipal = caCert.getSubjectX500Principal();
    System.out.printf("Issuer DN = %s.%n", issuerPrincipal);

    String orderedPrincipalString = reverse(issuerPrincipal.toString(), ",");

    final X500Principal orderedPrincipal = new X500Principal(orderedPrincipalString);

    System.out.printf("Ordered Issuer DN = %s%n", orderedPrincipal);

//    System.out.println("Using reversed Issuer DN");    
//    crlGen.setIssuerDN(orderedPrincipal);


    System.out.println("Using X500Principal from issuer Cert");
    crlGen.setIssuerDN(issuerPrincipal);


    crlGen.setThisUpdate(now);
    crlGen.setNextUpdate(new Date(now.getTime() + 100000));
    crlGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

    crlGen.addCRLEntry(revokedSerialNumber, now, reason /* CRLReason.privilegeWithdrawn */);

    crlGen.addExtension(X509Extension.authorityKeyIdentifier, false,
      new AuthorityKeyIdentifierStructure(caCert /* .getPublicKey() */));
    crlGen.addExtension(X509Extension.cRLNumber, false, new CRLNumber(BigInteger.valueOf(1)));

    return crlGen.generateX509CRL(caKey, "BC");

  }

  public DERSet createExtensionRequest(
    Vector<ASN1ObjectIdentifier> oids,
    Vector<X509Extension> values) {

    X509Extensions exts = new X509Extensions(oids, values);
    Attribute requestAttr = new Attribute(
      PKCSObjectIdentifiers.pkcs_9_at_extensionRequest,
      new DERSet(exts));

    return new DERSet(requestAttr);
  }

  public void addStringExtensionVector(
    final Vector<ASN1ObjectIdentifier> oids,
    final Vector<X509Extension> values,
    final ASN1ObjectIdentifier newOid,
    final DEROctetString newValue) {

    oids.add(newOid);

    values.add(new X509Extension(false, newValue));


  }

  public PKCS10CertificationRequest createCSR(
    final KeyPair keyPair,
    final String subject,
    final String signatureAlgorithm)
    throws IllegalArgumentException {


    PKCS10CertificationRequest request = null;


//
//      HashMap<String, String> extensionRequests = new HashMap<String, String>();
//
//      extensionRequests.put("1.34.5.0.14." + "67.99", "user-cert");

//
//      Vector<String> oids = new Vector<String> (extensionRequests.keySet());
//
//      Vector<String> values = new Vector<String> (extensionRequests.values());
//
//      DERSet extensionsAttributeSet = createExtensionRequestAttributeSet(oids, values);

    try {
      request = new PKCS10CertificationRequest(signatureAlgorithm, new X500Principal(subject),
        keyPair.getPublic(), null, keyPair.getPrivate());
    } catch (NoSuchAlgorithmException ex) {
      System.err.println(ex);
    } catch (NoSuchProviderException ex) {
      System.err.println(ex);
    } catch (InvalidKeyException ex) {
      System.err.println(ex);
    } catch (SignatureException ex) {
      System.err.println(ex);
    }

    return request;

  }

  public PKCS10CertificationRequest createServiceCSR(final KeyPair keyPair, final int days,
    final String hostname, final String type, final String description) {

    PKCS10CertificationRequest request = null;

    DERSet extensionsAttributeSet = null;

    Vector<ASN1ObjectIdentifier> oids = new Vector<ASN1ObjectIdentifier>();
    Vector<X509Extension> values = new Vector<X509Extension>();


//
//		oids.add( new ASN1ObjectIdentifier( "1.34.5.0.14." + "67.2" ) );

//		values.add( new X509Extension (false, new DEROctetString (  ("days=" + days ).getBytes()  )) );

    //        extensionRequests.put("1.34.5.0.14." + "67.2", "days=" + days);
    //      extensionRequests.put("1.34.5.0.14." + "67.101", "extra");
    //      ASN1Sequence altNames = new ASN1Sequence() ;
    //      GeneralName host1 = new GeneralName ( GeneralName.dNSName, hostname );
    //      GeneralName host2 = new GeneralName ( GeneralName.dNSName, hostname + "2" );

    GeneralNames subjectAltName = new GeneralNames(new GeneralName(GeneralName.dNSName, hostname));

    //     GeneralNames subjectAltNameArr = new GeneralNames( new GeneralName[] { host1, host2 } );

    addStringExtensionVector(oids, values, X509Extension.subjectAlternativeName, new DEROctetString(subjectAltName));

    oids.add(X509Extension.subjectAlternativeName);
    values.add(new X509Extension(false, new DEROctetString(subjectAltName)));

    extensionsAttributeSet = createExtensionRequest(oids, values);

//		DERSet extsAttrs = /* DERSet.getInstance */ (extensionsAttributeSet);
//		int wantedDays = -1;

//		X509Extensions extensions = Utils.getExtensions(extsAttrs);
//		X509Extension subAltName = extensions.getExtension( X509Extensions.SubjectAlternativeName  );

//		ASN1Object ob = X509Extension.convertValueToObject(subAltName);

    //      ASN1Sequence as = subAltName.convertValueToObject(subAltName);
    //     byte[] asnOctos = aOs.getOctets();

//		GeneralNames gn = GeneralNames.getInstance(ob);

    //   GeneralName[] names = gn.getNames();
    //     for ( GeneralName name: names) {
    //
    //       if ( name.getTagNo() == GeneralName.dNSName )
    //         System.out.printf ( "DNS Name = %s.%n",  name.getName() );
    //
    //     }

//		final String dnsName = Utils.getAltName( gn.getNames(), GeneralName.dNSName );

//		String certType = Utils.getExtensionValueByOID(extensions, "1.34.5.0.14." + "67.99");
    String commonName = hostname; // + "/" + type;
    X509Principal principal = new X509Principal("CN=" + commonName /* + ", O=" + description + ",OU=" + type */);

    try {

      request = new PKCS10CertificationRequest("SHA256withRSA", principal,
        keyPair.getPublic(), extensionsAttributeSet, keyPair.getPrivate());

    } catch (NoSuchAlgorithmException ex) {
      ;

    } catch (NoSuchProviderException ex) {
      ;

    } catch (InvalidKeyException ex) {
      ;

    } catch (SignatureException ex) {
      ;

    }
    return request;
  }

  public X509Certificate createRootCertificate(
    final KeyPair caKeyPair,
    final String caIssuer,
    final String signatureAlgorithm,
    final int lifetime,
    final int serial)
    throws
    CertificateException,
    InvalidKeyException {

    final String caSubject = caIssuer;

    final boolean isCA = true;

    try {

      return createCertificate(
        caKeyPair.getPublic(), caSubject, serial,
        caIssuer, caKeyPair, signatureAlgorithm, isCA,
        lifetime, 0, 0);

    } catch (OperatorCreationException ex) {
      throw new CertificateException(ex);
    }

  }

  /*
   * Supply the current time as a parameter to the full-argument version
   * 
   */
  public X509Certificate createCertificate(
    final PublicKey subjectPublicKey,
    final String subjectName,
    final int serial,
    final String issuerName,
    final KeyPair issuerKeyPair,
    final String signatureAlgorithm,
    final boolean isCA,
    final int days, final int hours, final int minutes)
    throws CertificateException,
    InvalidKeyException,
    OperatorCreationException {



    Calendar nowCal = Calendar.getInstance();

    Date origin = null;

    if (nowCal != null) {
      origin = nowCal.getTime();
    } else {
      System.err.println("Calendar.getInstance returned NULL");
    }

    if (origin == null) {

      System.err.println("Date origin is NULL");
    }

    return createCertificate(
      subjectPublicKey, subjectName,
      serial, issuerName, issuerKeyPair, signatureAlgorithm,
      isCA, origin,
      days, hours, minutes);
  }

  /*
   *
   * Create a certificate
   *
   *
   */
  public X509Certificate createHostCertificate(
    final PublicKey hostPublicKey, final String hostName,
    final BigInteger serial,
    final X509Certificate issuerCert, final PrivateKey issuerPrivateKey,
    final String signatureAlgorithm,
    final Date origin,
    final int days, final int hours, final int minutes)
    throws CertificateException,
    InvalidKeyException,
    OperatorCreationException {

    X509v3CertificateBuilder certBuilder;

    ContentSigner sigGen = null;

    sigGen = getContentSigner(issuerPrivateKey, signatureAlgorithm);

    X509CertificateHolder certHolder = null;

    X500Principal subject = new X500Principal("CN=" + hostName);

    Date[] beforeAndAfter = getValidityRange(days, hours, minutes);

    certBuilder = new JcaX509v3CertificateBuilder(issuerCert, serial,
      beforeAndAfter[0], beforeAndAfter[1], subject, hostPublicKey);

    addKeyIDExtensions(certBuilder, issuerCert.getPublicKey(), hostPublicKey);

    certBuilder.addExtension(X509Extension.basicConstraints, true, new BasicConstraints(false));

    GeneralNames subjectAltNames = new GeneralNames(new GeneralName(GeneralName.dNSName, hostName));
    certBuilder.addExtension(X509Extension.subjectAlternativeName, false, subjectAltNames.getDERObject());




    /*
     * Limit EEC key usage to TLS client auth and server auth 
     *
     * Spefify these in ExtendedKeyUsage extension
     *
     */

    Vector<KeyPurposeId> ekUsages = new Vector<KeyPurposeId>();

    ekUsages.add(KeyPurposeId.id_kp_clientAuth);
    ekUsages.add(KeyPurposeId.id_kp_serverAuth);

    certBuilder.addExtension(X509Extension.extendedKeyUsage, true,
      new ExtendedKeyUsage(ekUsages));



    certHolder = certBuilder.build(sigGen);

    return new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).
      getCertificate(certHolder);

  }

  /*
   *
   * Create a certificate
   *
   *
   */
  public X509Certificate createCertificate(
    final PublicKey subjectPublicKey, final String subjectName,
    final int serial, final String issuerName,
    final KeyPair issuerKeyPair, final String signatureAlgorithm,
    final boolean isCA, final Date origin,
    final int days, final int hours, final int minutes)
    throws CertificateException,
    InvalidKeyException,
    OperatorCreationException {

    X509v3CertificateBuilder certBuilder;

    ContentSigner sigGen = null;

    sigGen = getContentSigner(issuerKeyPair.getPrivate(), signatureAlgorithm);


    X509CertificateHolder certHolder = null;

    X500Name subject = new X500Name(subjectName);
    X500Name issuer = new X500Name(issuerName);

    Date[] beforeAndAfter = getValidityRange(days, hours, minutes);

    certBuilder = new JcaX509v3CertificateBuilder(issuer, BigInteger.valueOf(serial),
      beforeAndAfter[0], beforeAndAfter[1], subject, subjectPublicKey);

    //   new JcaX509v3CertificateBuilder(null, BigInteger.ZERO, origin, origin, null, subjectPublicKey);

    addKeyIDExtensions(certBuilder, issuerKeyPair.getPublic(), subjectPublicKey);

    certBuilder.addExtension(X509Extension.basicConstraints, true,
      new BasicConstraints(isCA));

    if (isCA) {

      /*
       * CA can sign Certificates and CRLs
       *
       * We can only specify this in KeyUsage extension
       *
       */

      certBuilder.addExtension(X509Extension.keyUsage, true,
        new X509KeyUsage(
        //        X509KeyUsage.digitalSignature
        //        | X509KeyUsage.keyEncipherment
        //        | X509KeyUsage.dataEncipherment
        //        | 
        X509KeyUsage.cRLSign
        | X509KeyUsage.keyCertSign));

    } else {

      /*
       * Limit EEC key usage to TLS client auth and server auth for now (i.e.
       * not signing yet, will be required for delegation)
       *
       * Spefify these in ExtendedKeyUsage extension
       *
       */

      Vector<KeyPurposeId> ekUsages = new Vector<KeyPurposeId>();

      ekUsages.add(KeyPurposeId.id_kp_clientAuth);
//      ekUsages.add(KeyPurposeId.id_kp_serverAuth);

      certBuilder.addExtension(X509Extension.extendedKeyUsage, true,
        new ExtendedKeyUsage(ekUsages));



    }

    certHolder = certBuilder.build(sigGen);

    return new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).
      getCertificate(certHolder);

  }
  
   public X509Certificate createUserCertificate(
    final PublicKey subjectPublicKey,
    final X500Principal subjectPrincipal,
    final String uuid,
    final BigInteger serial,
    final X509Certificate issuerCert,
    final PrivateKey issuerPrivateKey, final String signatureAlgorithm,
    final int days, final int hours, final int minutes)
    throws CertificateException,
    InvalidKeyException,
    OperatorCreationException {

	/* kltsa 28/02/2014, no issue : Removal of check so that any string can be used. */
	/*   
    if (!isUUID(uuid)) {

      throw new IllegalArgumentException(String.format("UUID %s invalid", uuid));

    }
    */
	   
    X509v3CertificateBuilder certBuilder;

    ContentSigner sigGen = getContentSigner(issuerPrivateKey, signatureAlgorithm); // Could be built in constructor

    Date[] beforeAndAfter = getValidityRange(days, hours, minutes);

    X500Principal subjectReversed = new X500Principal(reverse(subjectPrincipal.toString(), ","));

    certBuilder = new JcaX509v3CertificateBuilder(issuerCert, serial,
      beforeAndAfter[0], beforeAndAfter[1], subjectReversed,
      subjectPublicKey);

    addKeyIDExtensions(certBuilder, issuerCert.getPublicKey(), subjectPublicKey);

    certBuilder.addExtension(X509Extension.basicConstraints, true,
      new BasicConstraints(false));

    GeneralNames subjectAltNames = new GeneralNames(
      new GeneralName(GeneralName.uniformResourceIdentifier, "urn:uuid:" + uuid));
    certBuilder.addExtension(X509Extension.subjectAlternativeName, false, subjectAltNames.getDERObject());

    Vector<KeyPurposeId> ekUsages = new Vector<KeyPurposeId>();

    ekUsages.add(KeyPurposeId.id_kp_clientAuth);
//      ekUsages.add(KeyPurposeId.id_kp_serverAuth);

    certBuilder.addExtension(X509Extension.extendedKeyUsage, false,
      new ExtendedKeyUsage(ekUsages));

//    }

    X509CertificateHolder certHolder = certBuilder.build(sigGen);

    return new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).
      getCertificate(certHolder);

  } 

  public X509Certificate createUserCertificateWithSAML(
    final PublicKey subjectPublicKey,
    final X500Principal subjectPrincipal,
    final String uuid,
    final BigInteger serial,
    final X509Certificate issuerCert,
    final PrivateKey issuerPrivateKey, final String signatureAlgorithm,
    final int days, final int hours, final int minutes,
    final String oid, final boolean critical, final String value)
    throws CertificateException,
    InvalidKeyException,
    OperatorCreationException {

    if (!isUUID(uuid)) {

      throw new IllegalArgumentException(String.format("UUID %s invalid", uuid));

    }

    X509v3CertificateBuilder certBuilder;

    ContentSigner sigGen = getContentSigner(issuerPrivateKey, signatureAlgorithm); // Could be built in constructor

    Date[] beforeAndAfter = getValidityRange(days, hours, minutes);

    X500Principal subjectReversed = new X500Principal(reverse(subjectPrincipal.toString(), ","));

    certBuilder = new JcaX509v3CertificateBuilder(issuerCert, serial,
      beforeAndAfter[0], beforeAndAfter[1], subjectReversed,
      subjectPublicKey);

    addKeyIDExtensions(certBuilder, issuerCert.getPublicKey(), subjectPublicKey);

    certBuilder.addExtension(X509Extension.basicConstraints, true,
      new BasicConstraints(false));

    GeneralNames subjectAltNames = new GeneralNames(
      new GeneralName(GeneralName.uniformResourceIdentifier, "urn:uuid:" + uuid));
    certBuilder.addExtension(X509Extension.subjectAlternativeName, false, subjectAltNames.getDERObject());

    Vector<KeyPurposeId> ekUsages = new Vector<KeyPurposeId>();

    ekUsages.add(KeyPurposeId.id_kp_clientAuth);
//      ekUsages.add(KeyPurposeId.id_kp_serverAuth);

    certBuilder.addExtension(X509Extension.extendedKeyUsage, false,
      new ExtendedKeyUsage(ekUsages));

//    }
    
    if (value != null) {
      addStringExtension(certBuilder, oid, critical, value);   
    } 

    X509CertificateHolder certHolder = certBuilder.build(sigGen);

    return new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).
      getCertificate(certHolder);

  }

  public X509Certificate createUserCertificateWithCustomAttrs(
    final PublicKey subjectPublicKey,
    final X500Principal subjectPrincipal,
    final String uuid,
    final BigInteger serial,
    final X509Certificate issuerCert,
    final PrivateKey issuerPrivateKey, final String signatureAlgorithm,
    final int days, final int hours, final int minutes,
    final Map<String, String> customAttrs)
    throws CertificateException,
    InvalidKeyException,
    OperatorCreationException {

    if (!isUUID(uuid)) {

      throw new IllegalArgumentException(String.format("UUID %s invalid", uuid));

    }

    X509v3CertificateBuilder certBuilder;

    ContentSigner sigGen = getContentSigner(issuerPrivateKey, signatureAlgorithm); // Could be built in constructor

    Date[] beforeAndAfter = getValidityRange(days, hours, minutes);

    X500Principal subjectReversed = new X500Principal(reverse(subjectPrincipal.toString(), ","));

    certBuilder = new JcaX509v3CertificateBuilder(issuerCert, serial,
      beforeAndAfter[0], beforeAndAfter[1], subjectReversed,
      subjectPublicKey);

    addKeyIDExtensions(certBuilder, issuerCert.getPublicKey(), subjectPublicKey);

    certBuilder.addExtension(X509Extension.basicConstraints, true,
      new BasicConstraints(false));

    GeneralNames subjectAltNames = new GeneralNames(
      new GeneralName(GeneralName.uniformResourceIdentifier, "urn:uuid:" + uuid));
    certBuilder.addExtension(X509Extension.subjectAlternativeName, false, subjectAltNames.getDERObject());

    Vector<KeyPurposeId> ekUsages = new Vector<KeyPurposeId>();

    ekUsages.add(KeyPurposeId.id_kp_clientAuth);
//      ekUsages.add(KeyPurposeId.id_kp_serverAuth);

    certBuilder.addExtension(X509Extension.extendedKeyUsage, false,
      new ExtendedKeyUsage(ekUsages));

//    }

    if (customAttrs != null) {
      for (Map.Entry<String, String> entry : customAttrs.entrySet()) {
        addStringExtension(certBuilder, entry.getKey(), false, entry.getValue());
      }
    }

    X509CertificateHolder certHolder = certBuilder.build(sigGen);

    return new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).
      getCertificate(certHolder);
  }

  public String[] getBasicAuthUsernamePassword(final String authHeader) {

    String[] usernameAndPassword = null;

    final String BASIC = "Basic ";

    if (authHeader != null) {

      final String basicAuthEncoded = authHeader.substring(BASIC.length());

      if (basicAuthEncoded != null) {
        final String basicAuthAsString = new String(
          Base64.decode(basicAuthEncoded.getBytes()));

        usernameAndPassword = basicAuthAsString.split(":");

//        if (usernameAndPassword.length != 2) {
//          usernameAndPassword = null;
//        }

      }
    }

    return usernameAndPassword;

  }
  
  

  public Date[] getValidityRange(final int days, final int hours, final int minutes)
    throws IllegalArgumentException {
    return getValidityRange(Calendar.getInstance(), days, hours, minutes);

  }

  public Date[] getValidityRange(
    final Calendar origin,
    final int days,
    final int hours,
    final int minutes)
    throws IllegalArgumentException {

    /*
     * Should we use assert ( days >= 0 && hours >= 0 && minutes >= 0 ) - ?
     * 
     */

    if (days < 0 || hours < 0 || minutes < 0) {

      throw new IllegalArgumentException("Arguments to setCertValidity cannot be negative");

    }

    Date[] beforeAndAfter = new Date[2];

    Calendar now = (Calendar) origin.clone();

    now.add(Calendar.MINUTE, -5);	// Allow 5 min time lag (slow)	

    beforeAndAfter[0] = now.getTime();

    now.add(Calendar.DAY_OF_YEAR, days);
    now.add(Calendar.HOUR_OF_DAY, hours);
    now.add(Calendar.MINUTE, minutes + 10);	// Allow a 5 min time lag (fast)

    beforeAndAfter[1] = now.getTime();

    return beforeAndAfter;

  }

  public void addStringExtension(final X509v3CertificateBuilder builder, final String oid,
    final boolean critical, final String value) {
    
    if (value != null) {
    builder.addExtension(new ASN1ObjectIdentifier(oid), critical, 
      new DEROctetString(value.getBytes()));
    } else { 
      System.err.printf("Not adding XML value");
    }

  }
  
  

  /*
   * 
   * From stackoverflow.com
   * 
   */
  public String getExtensionValueAsOctetString(X509Certificate X509Certificate, String oid) throws IOException {
    String decoded = null;
    byte[] extensionValue = X509Certificate.getExtensionValue(oid);

    if (extensionValue != null) {

      DERObject derObject = toDERObject(extensionValue);

      if (derObject instanceof DEROctetString) {

        DEROctetString derOctetString = (DEROctetString) derObject;

        derObject = derOctetString.getDERObject();

        decoded = derObject.toString();  // TODO: Need to read as from an ASN1ObjectStream

      } else {
        System.err.println("derOjbect is not a DERObjectString.%n");
      }
    } else {
      System.err.println("extension value is NULL.%n");
    }
    return decoded;
  }

  public DERObject toDERObject(byte[] data) throws IOException {
    ByteArrayInputStream inStream = new ByteArrayInputStream(data);
    ASN1InputStream asnInputStream = new ASN1InputStream(inStream);

    return asnInputStream.readObject();

  }

  public String reverse(final String in, final String seperator) {

    String[] split = in.split(seperator);

    ArrayUtils.reverse(split);

    return StringUtils.join(split, seperator);

  }

  public int getIntegerProperty(Properties props, String key, int defaultValue) {

    int value = defaultValue;

    String propVal = props.getProperty(key);

    if (null != propVal) {
      value = Integer.valueOf(propVal);
    }

    return value;
  }

  public KeyPair generateKeyPair(final String algorithm, final int keylen)
    throws NoSuchAlgorithmException {

    KeyPairGenerator kpGen = KeyPairGenerator.getInstance(algorithm);

    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

    kpGen.initialize(keylen, random);

    return kpGen.generateKeyPair();

  }

  public int getRequestedDuration(String requestedPeriod, String periodType) {

    int duration = 0;

    String[] keyAndValue = requestedPeriod.split("=");

    if (keyAndValue.length == 2 && periodType.equals(keyAndValue[0])) {

      try {
        duration = Integer.valueOf(keyAndValue[1]);
      } catch (NumberFormatException ex) {
        ;
      }

    }

    return duration;

  }

  public int getDays(final X509Extensions extensions) {

    int wantedDays = 0;

    final String XTREEMOS_ARC = "1.34.5.0.14.";

    final String validity = XTREEMOS_ARC + "67.2";

    X509Extension durationExtension = extensions.getExtension(new ASN1ObjectIdentifier(validity));


    if (durationExtension != null) {

      String wanted = getExtensionValue(durationExtension);

      wantedDays = getRequestedDuration(wanted, "days");

    }
    return wantedDays;
  }

  public String expandTilde(final String filename)
    throws FileNotFoundException {

    assert filename != null : "filename is null";
    assert filename.length() > 0 : "filename is zero-length";

    String actualFilename = filename;

    if (filename.charAt(0) == '~') {

      if (filename.length() >= 3) {

        if (filename.charAt(1) != '/') { //  '~', '/', 'X' - shortest filename relative to '~'

          throw new FileNotFoundException("Cannot expand '~user' notation in '" + filename + "'. Use '~/' for your home directory.");

        } else {  // We have at least the shortest possible short-cut using user's home directory

          actualFilename = filename.replaceAll("\\~", System.getProperty("user.home"));

        }


      } else { // Length of filename is < 3 chars
        throw new FileNotFoundException("Abbreviated filename is too short to use.");
      }

    }

    return actualFilename;

  }

  public String readLine(final String filename)
    throws FileNotFoundException, IOException, AssertionError {

    assert filename != null : "Filename is NULL";
    assert filename.length() > 0 : "Filename is zero-length";

    String actualFilename = expandTilde(filename);

    BufferedReader reader = new BufferedReader(new FileReader(actualFilename));

    String line = reader.readLine();

    reader.close();

    if (line == null || line.length() == 0) {

      throw new IOException("Cannot read from " + actualFilename);

    }
    return line.trim();

  }

  public String getExtensionValueByOID(final X509Extensions extensions, final String oid) {

    String value = null;


    ASN1ObjectIdentifier asn1oid = new ASN1ObjectIdentifier(oid);
    //X509Extension extension = extensions.getExtension(ASN1OID);

    X509Extension extension = extensions.getExtension(asn1oid);

    if (extension != null) {

      value = getExtensionValue(extension);

    }

    return value;
  }
  
  public String getSAMLAssertion(final X509Certificate cert)
    throws IOException {

    return getStringExtensionValue(cert, CONTRAIL_ATTRIBUTE_ASSERTION);

  }

  public String getStringExtensionValue(final X509Certificate cert, final String oid)
    throws IOException {

    String assertion = null;

    byte[] bytes = cert.getExtensionValue(oid);

    ASN1InputStream a1n = new ASN1InputStream(bytes);

    ASN1OctetString extnValue = (ASN1OctetString) a1n.readObject();

    a1n = new ASN1InputStream(extnValue.getOctets());

    DERObject extensionType = a1n.readObject();

    if (extensionType instanceof DEROctetString) {

      DEROctetString derOS = (DEROctetString) extensionType;

      assertion = new String(derOS.getOctets());

    }

    return assertion;

  }  

  public String getExtensionValue(X509Extension ext) {

    ASN1OctetString aOs = ext.getValue();

    byte[] asnOctos = aOs.getOctets();

    return new String(asnOctos);

  }

  public char[] getPass(final String passSource)
    throws FileNotFoundException, IOException {

    char[] pass = null;


    if (passSource != null) {

//      final int MAX_PASSLEN = 80;
//      pass = new char[MAX_PASSLEN];
      final String SRC_PREFIX = "file:";

      if (passSource.startsWith(SRC_PREFIX)) {

        final String filename = passSource.substring(SRC_PREFIX.length());

        final String line = readLine(filename);
        pass = line.toCharArray();

      } else {

        pass = passSource.toCharArray();

      }

    }

    return pass;

  }

  public void writeKey(final String userKeyFilename, final PrivateKey priv, char[] keyPassphrase)
    throws FileNotFoundException, IOException, IllegalArgumentException {

    writeKey(userKeyFilename, priv, keyPassphrase, "DESEDE");

  }

  public void writeKey(final String userKeyFilename, PrivateKey priv, char[] keyPassphrase, final String alg)
    throws FileNotFoundException, IOException, IllegalArgumentException {

    if (priv == null) {
      throw new IllegalArgumentException("Key is NULL");
    }

    if (keyPassphrase.length == 0) {
      throw new IllegalArgumentException("keyPassPhrase is 0 elements long");

    }

    File f = new File(userKeyFilename);

    f.createNewFile();
    f.setReadable(false, false); // Turns off 'r' for group and other
    f.setReadable(true, true);  // Turns 'r' back on for user

    FileOutputStream fos = new FileOutputStream(f);

    writeKey(fos, priv, alg, keyPassphrase);

    fos.close();

  }

  public void writeKeyPair(final String userKeyFilename, final KeyPair keyPair, char[] keyPassphrase, final String alg)
    throws FileNotFoundException, IOException, IllegalArgumentException {

    if (keyPair == null) {
      throw new IllegalArgumentException("Key is NULL");
    }

    if (keyPassphrase.length == 0) {
      throw new IllegalArgumentException("keyPassPhrase is 0 elements long");

    }

    File f = new File(userKeyFilename);

    f.createNewFile();
    f.setReadable(false, false); // Turns off 'r' for group and other
    f.setReadable(true, true);  // Turns 'r' back on for user

    FileOutputStream fos = new FileOutputStream(f);

    writeKeyPair(fos, keyPair, alg, keyPassphrase);

    fos.close();

  }

  public byte[] digest(
    final byte[] message,
    final String algorithm)
    throws NoSuchAlgorithmException, NoSuchProviderException {

    MessageDigest md = MessageDigest.getInstance(algorithm, "BC");

    return md.digest(message);

  }

  public byte[] sha256digest(
    final byte[] message)
    throws NoSuchAlgorithmException, NoSuchProviderException {

    return digest(message, "SHA-256");

  }

  public void writeCertificate(final X509Certificate userCert, final String userCertFilename)
    throws IOException {

    OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(userCertFilename));
    try {
      writeCertificate(osw, userCert);
    } catch (Exception e) {
      throw new IOException("Cannot write certificate to file '" + userCertFilename + "'");
    }

    osw.close();

  }

  public CRLDistPoint createCRLDistPoint(
    final String[] crlURIs) {

    CRLDistPoint crlDistPoint = null;

    if (crlURIs != null && crlURIs.length != 0) {
      ASN1EncodableVector asn1ev = new ASN1EncodableVector();

      for (String uri : crlURIs) {

        asn1ev.add(
          new GeneralName(GeneralName.uniformResourceIdentifier,
          new DERIA5String(uri)));

      }

      GeneralNames gns = new GeneralNames(new DERSequence(asn1ev));

      final int FULL_NAME = 0;

      DistributionPointName dpn = new DistributionPointName(FULL_NAME, gns);
      DistributionPoint distp = new DistributionPoint(dpn, null, null);

      crlDistPoint = new CRLDistPoint(new DistributionPoint[]{distp});

    }

    return crlDistPoint;
  }

  public void writeCertificateChain(
    final X509Certificate[] certChain,
    final String userCertFilename)
    throws IOException {

    OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(userCertFilename));
    try {
      for (X509Certificate cert : certChain) {
        writeCertificate(osw, cert);
      }
      osw.close();

    } catch (IOException e) {
      throw new IOException("Cannot write certificate to file '" + userCertFilename + "'");
    }

  }

  public String writeCSR(
    final PKCS10CertificationRequest request)
    throws IOException {

    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    final PEMWriter pemWriter = new PEMWriter(new OutputStreamWriter(baos));

    pemWriter.writeObject(request);
    pemWriter.flush();

    return baos.toString();
  }

  public void writeCSR(
    final OutputStream out,
    final PKCS10CertificationRequest request)
    throws IOException {

    final PEMWriter pemWriter = new PEMWriter(new OutputStreamWriter(out));

    pemWriter.writeObject(request);
    pemWriter.flush();

  }

  public void writeCSR(
    final PKCS10CertificationRequest request,
    final String csrFilename)
    throws IOException {

    writeCSR(new FileOutputStream(csrFilename), request);

  }

  public String getUsername(Console console) {

    return console.readLine("Username: ");

  }

  public void writeCRL(
    final OutputStream out,
    final X509CRL crl)
    throws IOException {

    final PEMWriter pemWriter = new PEMWriter(new OutputStreamWriter(out));

    pemWriter.writeObject(crl);
    pemWriter.flush();

  }

  public char[] getPassphrase(
    final Console console,
    final int minLength)
    throws
    IllegalArgumentException {

    char[] password = null;

    char[] inpass = null;

    inpass = console.readPassword(
      "Passphrase to protect private key (at least %d characters long): ", minLength);

    if (inpass == null || inpass.length < minLength) {
      throw new IllegalArgumentException("Passphrase is too short");
    } else {    // read into inpass OK

      password = inpass;

      inpass = console.readPassword("%s", "Type passphrase again to confirm: ");

      if (inpass == null || inpass.length < minLength) {
        throw new IllegalArgumentException("Confirmation passphrase is too short");
      } else {

        if (!Arrays.equals(inpass, password)) {
          throw new IllegalArgumentException("Passphrases do not match");
        }

      }
    }

    return password;
  }

  public X509Extensions getExtensions(
    final ASN1Set attributes) {

    X509Extensions extensions = null;

    for (int i = 0; i != attributes.size(); i++) {

      Attribute attr = Attribute.getInstance(attributes.getObjectAt(i));

      if (PKCSObjectIdentifiers.pkcs_9_at_extensionRequest.getId().
        equals(attr.getAttrType().getId())) {

        extensions = X509Extensions.getInstance(attr.getAttrValues().getObjectAt(0));
        break;

      }

    }
    return extensions;

  }

  public String findSubjectAltName(
    final Collection subjectAltNames,
    final int nameType) {

    String subjectAltName = null;

    for (Iterator i = subjectAltNames.iterator(); i.hasNext();) {

      List item = (List) subjectAltNames.iterator().next();
      Integer type = (Integer) item.get(0);
      if (type == nameType) {
        subjectAltName = (String) item.get(1);
        break;
      }

    }

    return subjectAltName;

  }

  public String getAltName(final GeneralName[] names, final int tagNo) {

    String altName = null;

//       GeneralNames gn = GeneralNames.getInstance(ob);

//     GeneralName[] names = gn.getNames();

    for (GeneralName name : names) {

      if (name.getTagNo() == tagNo /* GeneralName.dNSName */) {
        altName = name.getName().toString();
        break;
      }

    }


    return altName;
  }

  public Object readPEM(
    final InputStream in,
    final char[] password)
    throws  IOException, NoSuchAlgorithmException {
    
  
    PEMReader reader;
    PrivateKey key = null;

    if (password == null) {

      reader = new PEMReader(new InputStreamReader(
        in));

    } else {

      reader = new PEMReader(new InputStreamReader(
        in),
        new PasswordFinder() {
          @Override
          public char[] getPassword() {

            return password;
          }
        });
      
    } 
    
    if (reader == null) {
      throw new IOException("PEM reader is null");
    }
    Object o = reader.readObject();
    reader.close();
    
    return o;
  }

   public PrivateKey readPrivateKey(
    final InputStream in,
    final char[] password)
    throws FileNotFoundException, IOException, NoSuchAlgorithmException {


    PrivateKey key = null;
    Object o = readPEM(in, null);

    if (o == null) {
      
      throw new IOException("readPEM returns NULL");
      
    }
    if (o instanceof PrivateKey) {

      key = (PrivateKey) o;

    } else if (o instanceof KeyPair) {

      key = ((KeyPair) o).getPrivate();
    }

    return key;

  } 
  
  public PrivateKey readPrivateKey(
    final String keyFilename,
    final char[] password)
    throws FileNotFoundException, IOException, NoSuchAlgorithmException {


    PrivateKey key = null;
    Object o = readPEM(keyFilename, null);

    if (o == null) {
      
      throw new IOException("readPEM returns NULL");
      
    }
    if (o instanceof PrivateKey) {

      key = (PrivateKey) o;

    } else if (o instanceof KeyPair) {

      key = ((KeyPair) o).getPrivate();
    }

    return key;

  }

  public KeyPair readKeyPair(
    final String keyFilename,
    final char[] password)
    throws FileNotFoundException, IOException, NoSuchAlgorithmException {

    return (KeyPair) readPEM(keyFilename, password);


  }
  
  @Deprecated
  protected class MyPasswordZZZ implements PasswordFinder {

    private char[] password;

    public MyPasswordZZZ(char[] password) {
      this.password = password.clone();
    }

    @Override
    public char[] getPassword() {
      return password.clone();
    }
  }  

  public Object readPEM(
    final String keyFilename,
    final char[] password)
    throws FileNotFoundException, IOException, NoSuchAlgorithmException {

    PEMReader reader;

    if (password == null) {

      reader = new PEMReader(new InputStreamReader(
        new FileInputStream(keyFilename)));

    } else {

      reader = new PEMReader(new InputStreamReader(
        new FileInputStream(keyFilename)),
        new PasswordFinder() {
          @Override
          public char[] getPassword() {

            return password;
          }
        });

    }

    final Object obj = reader.readObject();
    reader.close();

    return obj;

  }

  /*
   * TODO: Replace with calls to CertFactory?
   * 
   */
  public PKCS10CertificationRequest readCSR(
    final InputStream is)
    throws IOException {

    return (PKCS10CertificationRequest) readPEM(new InputStreamReader(is));

  }
  /*
   *
   *
   */

  public PKCS10CertificationRequest readCSR(String csrInFilename)
    throws FileNotFoundException, IOException {

    return (PKCS10CertificationRequest) readPEM(new InputStreamReader(new FileInputStream(csrInFilename)));
  }

  public String readURL(final URL url)
    throws IOException {

    String content = null;

    URLConnection conn = null;
    BufferedInputStream bis = null;

    try {

      conn = url.openConnection();
      bis = new BufferedInputStream(conn.getInputStream());

      byte[] buf = new byte[bis.available()];
      bis.read(buf);
      content = new String(buf);

    } finally {

      if (bis != null) {
        try {
          bis.close();
        } catch (IOException ex) {
          ;
        }
      }


    }
    return content;
  }

  public X509Certificate getCertFromStream(final InputStream is)
    throws CertificateException {


    X509Certificate cert = null;

    CertificateFactory cf = CertificateFactory.getInstance("X509");
    cert = (X509Certificate) cf.generateCertificate(is);

    return cert;

  }

  public X509Certificate readCertificate(InputStreamReader isr)
    throws IOException {

    return (X509Certificate) readPEM(isr);

  }

  public Object readPEM(
    final InputStreamReader isr)
    throws IOException {


    PEMReader preader = null;
    Object o = null;


    preader = new PEMReader(isr);
    o = preader.readObject();
    if (o == null) {
      throw new IOException("Read a NULL PEM Object");

    }
    return o;


  }

  public void writeKeyPair(
    final OutputStream os, //NOPMD
    final KeyPair key,
    final String alg,
    final char[] password)
    throws IOException {

    final PEMWriter keyWriter = new PEMWriter(new OutputStreamWriter(os));
    keyWriter.writeObject(key, alg, password, new SecureRandom());
    keyWriter.flush();

  }

  public void writeKey(
    final OutputStream os, //NOPMD
    final PrivateKey key,
    final String alg,
    final char[] password)
    throws IOException {

    final PEMWriter keyWriter = new PEMWriter(new OutputStreamWriter(os));
    keyWriter.writeObject(key, alg, password, new SecureRandom());
    keyWriter.flush();

  }

  public void writeKey(
    final OutputStream os, //NOPMD
    final PrivateKey key)
    throws IOException {

    final PEMWriter keyWriter = new PEMWriter(new OutputStreamWriter(os));
    keyWriter.writeObject(key);
    keyWriter.flush();

  }

  public void writeCertificate(
    final OutputStream out,
    final X509Certificate cert)
    throws IOException {

    final PEMWriter pemWriter = new PEMWriter(new OutputStreamWriter(out));
    pemWriter.writeObject(cert);
    pemWriter.flush();

  }

  public void writeCertificate(
    final OutputStreamWriter osw,
    final X509Certificate cert)
    throws IOException {

    final PEMWriter pemWriter = new PEMWriter(osw);
    pemWriter.writeObject(cert);
    pemWriter.flush();

  }
//  public  KeyPair readPrivateKey(final String keyFilename, final char[] keyPassphrase) 
//    throws BadPaddingException, IOException, FileNotFoundException {
//  
//    PasswordFinder myPass = new PasswordFinder() {
//
//      @Override
//      public char[] getPassword() {
//        return keyPassphrase; 
//      }
//      
//    };
//
//    
//    KeyPair kp = null;
//    
//    
//    try {
//       
//      File keyFile = new File(keyFilename);
//            
//      FileInputStream fin = new FileInputStream(keyFile);
//   
//      PEMReader reader = new PEMReader(
//        new InputStreamReader(fin), myPass);
//           
//      kp = (KeyPair) reader.readObject(); 
//             
//      reader.close(); 
//    
//    }
//
//    catch (IOException ex) {
//      String msg = ex.getMessage();
//
//        if (msg.indexOf("BadPaddingException") > 0) {
//
//          throw new BadPaddingException("Possible wrong passphrase for private key in " + keyFilename);
//
//        } else {
//
//        throw ex;
//      }
//      
//    }
//    
//    return kp;
//      
//  }
}