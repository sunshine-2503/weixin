package com.shaoming.weixin.util;

/**
 * 支付宝工具类
 * Created by jerry on 17-10-23.
 */
public class AlipayUtil {
    // 支付宝网关
    public static String ALIPAY_URL = "https://openapi.alipay.com/gateway.do";
    // 应用id
    public static String APPID = "2017102009414859";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5FCNIJD9JqKVcLUb1L5H9TFgSdwUwn+6m2EVumMIFAL6s1nlE4qrMlvZ9s9kAaH+zReHKAUlgCac2hJ2TBh4gpG0sqqyyIuwoEulqqfWIYyR6TrH3nqBDN4NHQtZcTdJE0OH8mkcmoDlIWftE2qBIlPL1UEwwirAjE0aKV3zESP9f57TJjqhamnL3IoPcIKesOclvgWSlvz9eFlwL1PuY8i3z9kjUgiadwWgsE38ihFBUxyc2MDiFGqR2UOEo9Z41zbuoFfmBU/lClrjmXQ2UPf+qImMOwFN7OASsWEdb77dOUcU/yzRmu2/Zfm+FwtlwE9Gn9N0ldIx9qkNl/X1aQIDAQAB";
    // 开发者私钥
    public static String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCaNsOp6QEAwKPYyS7vf/hGfFlkwsOlv9Y9UbBJW+eI/alsxPg7zAhZY4zB4bsrHKSQ47n0HO9bOgBHsBsMKPhe7gJkjyn7INSnvhCUa1836WPtSqvEgxa6fRfEmUkUMVBCLulb7mhk3LeKhKBrIiSyDPAk518QujwkTUmNK6MaXXgQhqhSgNlJFmPHGw/DK4f04x5ViJCRKLutSA2Yv8peVDnkySqQslkn3A7sV3MTXcYeDCImUz0M1zGlxiOfC+0Bkf2U5QFcm1JfUvouvzHcW6kEagFjck+UoByPzdqiImdyAvLe32GhhqEtPSjUe6jhufW4VVyNcgIq8TEDktQ1AgMBAAECggEAIja8OlojitMyhso7y48MujakbqJ3XbKuCXSyM9RcDhyZFjStDjc6MvrPI8S9DdeQ+0RK4Un6ILdBkKYVmiEM2aLcjQrmOX0TMRQOAZ/AxFrXfLHwYezjggR23XtUp1I5iDV4OJkwOBBv+B9sp9gvZAFS1h0vDjsDVNqA0v03doWlhTCLRjtd4lFQFObQCEm+QZOgeLbCTCaxj7zz4fLRzLxUg8ZvKdjDKRj1hfjaEJSjvTGFDeUJ7rt0BCh22KI/UnXzAOuSp2awPif/JuQxa5WBMsAglmBUC+S0FrwzlhSl9+l2+aTCrt5pbeIfM73rb5qxtBuepL0kVTgjjPB5EQKBgQDd7pL2ZGx1LKlsVq4Ma9aVQ42ix2Atxcxalyx7ypfxb6qiGwd+uRw5D7UArLzSpC1wvXaMDkfZxxF4+ANMPq5M54RZluRRGDjPyq7btA/iW3hbnp0m4/bR+qT+9TFOTJ1y7Wb3D41B3uuDa2ysRgbuKjRoMRBhyVvgZAXJBMyPDwKBgQCx4wZZkP/3IbjGCADAT5/EOSc/MaGJaEDh/op/cX9SSOlgkmt87uYcrl63gH7Ov+uXebRStMbdrXxt9pLT+gRTUxOH128f6Tq5taq3UvnQ832Xw3GpuATHi5nJYkPpL7I9DB6k61rr7tB+hI0jyVANOtRWQ8cUQQbUnlh7shRoewKBgQC6V134GdvjCqv6tlEPRuB2lbDv0z3TUPhDvBjym+m2I89+qvlBEsY6txCAT4l/x7ALsFlAQaFcnsPx8TQ7qYmKMv4yhvqPiKny9riY6nhH1AubyfMju1b7edtd45wRpUyyOdkxalJt03Gl9+XYRDf/c1prfX7GzF4ja3hcD3Z+SwKBgHfWDxMLocRkzUtrXMYxXdoc/pmN39rnhr44cikE1tciE0ZhnVPexNqRhXFteP/jPt0euiH1cvsnwml58NQo2/0ePEwJD+2Ze1xcTBOQLIddYUB6A/pn46BiJUoLjuBqp1KE9af4fZnvmjmBJmvzVKB0/1LV+ilk3SsjOUyrXSA9AoGAdCylgDnAcerK+0gJi2Fqj6HvJEMPvSPvcMyrOx3cmY5d/5SZrh10c15BWAKga3Nb1d+2lijhe9b+/rKKo/ZX4XQTpzVqs6v49Wq5lasrQ1W+uCH/I1UgBaEZdsX5HUN0HqCIFQ66YEoKzVxqI7TrJt6467EjJvTCV/4Fbp4soZY=";
    // 编码格式
    public static String CHARSET = "GBK";
    // 签名算法类型
    public static String SIGN_TYPE = "RSA2";
    // 授权后回调链接
    public static String REDIRECT_URI = "http://shaoming.free.ngrok.cc/user/alipayCallBack";
    // 应用授权
    public static String APP_AUTHORIZE = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=APPID&scope=SCOPE&redirect_uri=REDIRECT_URI";

}
