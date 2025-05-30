package com.contacts.phonecontact.phonebook.dialer.DataHelper.DataFetchCode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.contacts.phonecontact.phonebook.dialer.R;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.Utils.FileExtUtils;
//import com.contacts.phonecontact.phonebook.dialer.Utils.VcfExporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

//import ezvcard.property.Kind;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.text.StringsKt;

public class ShareContactHelper {

//    public void invoke(Context context, List<Contact> list) {
//        String str;
//        if (list.size() == 1) {
//            str = ((Contact) CollectionsKt.first((List) list)).getNameToDisplay() + ".vcf";
//        } else {
//            str = "contacts.vcf";
//        }
//        File tempFile = getTempFile(context, str);
//        if (tempFile == null) {
//            Toast.makeText(context, (int) R.string.unknown_error_occurred, Toast.LENGTH_SHORT).show();
//        } else {
//            try {
//                new VcfExporter().exportContacts((Activity) context, new FileOutputStream(tempFile), list, new Function1<VcfExporter.ExportResult, Unit>() {
//                    @Override
//                    public Unit invoke(VcfExporter.ExportResult exportResult) {
//                        if (exportResult == VcfExporter.ExportResult.EXPORT_OK) {
//                            String absolutePath = tempFile.getAbsolutePath();
//                            Uri cachePhotoUri = FileExtUtils.getCachePhotoUri(context, tempFile);
//                            sharePathIntent(context, absolutePath, cachePhotoUri);
//                        }
//                        return Unit.INSTANCE;
//                    }
//                });
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void sharePathIntent(Context context, String str, Uri uri) {
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.SEND");
//        intent.putExtra("android.intent.extra.STREAM", uri);
//        intent.setType(getUriMimeType(context, str, uri));
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        try {
//            context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_via)));
//        } catch (Exception exception) {
//        }
//    }
//
//    public String getUriMimeType(Context context, String str, Uri uri) {
//        String mimeType = getMimeType(str);
//        return mimeType.length() == 0 ? getMimeTypeFromUri(context, uri) : mimeType;
//    }
//
//    public File getTempFile(Context context, String str) {
//        File file = new File(context.getCacheDir(), "contactCache");
//        if (file.exists() || file.mkdir()) {
//            return new File(file, str);
//        }
//        return null;
//    }
//
//    public String getMimeTypeFromUri(Context context, Uri uri) {
//        String str;
//        String path = uri.getPath();
//        String str2 = "";
//        if (path == null || (str = getMimeType(path)) == null) {
//            str = str2;
//        }
//        if (!(str.length() == 0)) {
//            return str;
//        }
//        try {
//            String type = context.getContentResolver().getType(uri);
//            if (type != null) {
//                str2 = type;
//            }
//            return str2;
//        } catch (IllegalStateException exception) {
//            return str;
//        }
//    }
//
//    public String getMimeType(String str) {
//        HashMap hashMap = new HashMap();
//        hashMap.put("323", "text/h323");
//        hashMap.put("3g2", "video/3gpp2");
//        hashMap.put("3gp", "video/3gpp");
//        hashMap.put("3gp2", "video/3gpp2");
//        hashMap.put("3gpp", "video/3gpp");
//        hashMap.put("7z", "application/x-7z-compressed");
//        hashMap.put("aa", "audio/audible");
//        hashMap.put("aac", "audio/aac");
//        hashMap.put("aaf", "application/octet-stream");
//        hashMap.put("aax", "audio/vnd.audible.aax");
//        hashMap.put("ac3", "audio/ac3");
//        hashMap.put("aca", "application/octet-stream");
//        hashMap.put("accda", "application/msaccess.addin");
//        hashMap.put("accdb", "application/msaccess");
//        hashMap.put("accdc", "application/msaccess.cab");
//        hashMap.put("accde", "application/msaccess");
//        hashMap.put("accdr", "application/msaccess.runtime");
//        hashMap.put("accdt", "application/msaccess");
//        hashMap.put("accdw", "application/msaccess.webapplication");
//        hashMap.put("accft", "application/msaccess.ftemplate");
//        hashMap.put("acx", "application/internet-property-stream");
//        hashMap.put("addin", "text/xml");
//        hashMap.put("ade", "application/msaccess");
//        hashMap.put("adobebridge", "application/x-bridge-url");
//        hashMap.put("adp", "application/msaccess");
//        hashMap.put("adt", "audio/vnd.dlna.adts");
//        hashMap.put("adts", "audio/aac");
//        hashMap.put("afm", "application/octet-stream");
//        hashMap.put("ai", "application/postscript");
//        hashMap.put("aif", "audio/aiff");
//        hashMap.put("aifc", "audio/aiff");
//        hashMap.put("aiff", "audio/aiff");
//        hashMap.put("air", "application/vnd.adobe.air-application-installer-package+zip");
//        hashMap.put("amc", "application/mpeg");
//        hashMap.put("anx", "application/annodex");
//        hashMap.put("apk", "application/vnd.android.package-archive");
//        hashMap.put(Kind.APPLICATION, "application/x-ms-application");
//        hashMap.put("art", "image/x-jg");
//        hashMap.put("asa", "application/xml");
//        hashMap.put("asax", "application/xml");
//        hashMap.put("ascx", "application/xml");
//        hashMap.put("asd", "application/octet-stream");
//        hashMap.put("asf", "video/x-ms-asf");
//        hashMap.put("ashx", "application/xml");
//        hashMap.put("asi", "application/octet-stream");
//        hashMap.put("asm", "text/plain");
//        hashMap.put("asmx", "application/xml");
//        hashMap.put("aspx", "application/xml");
//        hashMap.put("asr", "video/x-ms-asf");
//        hashMap.put("asx", "video/x-ms-asf");
//        hashMap.put("atom", "application/atom+xml");
//        hashMap.put("au", "audio/basic");
//        hashMap.put("avi", "video/x-msvideo");
//        hashMap.put("axa", "audio/annodex");
//        hashMap.put("axs", "application/olescript");
//        hashMap.put("axv", "video/annodex");
//        hashMap.put("bas", "text/plain");
//        hashMap.put("bcpio", "application/x-bcpio");
//        hashMap.put("bin", "application/octet-stream");
//        hashMap.put("bmp", "image/bmp");
//        hashMap.put("c", "text/plain");
//        hashMap.put("cab", "application/octet-stream");
//        hashMap.put("caf", "audio/x-caf");
//        hashMap.put("calx", "application/vnd.ms-office.calx");
//        hashMap.put("cat", "application/vnd.ms-pki.seccat");
//        hashMap.put("cc", "text/plain");
//        hashMap.put("cd", "text/plain");
//        hashMap.put("cdda", "audio/aiff");
//        hashMap.put("cdf", "application/x-cdf");
//        hashMap.put("cer", "application/x-x509-ca-cert");
//        hashMap.put("cfg", "text/plain");
//        hashMap.put("chm", "application/octet-stream");
//        hashMap.put("class", "application/x-java-applet");
//        hashMap.put("clp", "application/x-msclip");
//        hashMap.put("cmd", "text/plain");
//        hashMap.put("cmx", "image/x-cmx");
//        hashMap.put("cnf", "text/plain");
//        hashMap.put("cod", "image/cis-cod");
//        hashMap.put("config", "application/xml");
//        hashMap.put("contact", "text/x-ms-contact");
//        hashMap.put("coverage", "application/xml");
//        hashMap.put("cpio", "application/x-cpio");
//        hashMap.put("cpp", "text/plain");
//        hashMap.put("crd", "application/x-mscardfile");
//        hashMap.put("crl", "application/pkix-crl");
//        hashMap.put("crt", "application/x-x509-ca-cert");
//        hashMap.put("cs", "text/plain");
//        hashMap.put("csdproj", "text/plain");
//        hashMap.put("csh", "application/x-csh");
//        hashMap.put("csproj", "text/plain");
//        hashMap.put("css", "text/css");
//        hashMap.put("csv", "text/csv");
//        hashMap.put("cur", "application/octet-stream");
//        hashMap.put("cxx", "text/plain");
//        hashMap.put("dat", "application/octet-stream");
//        hashMap.put("datasource", "application/xml");
//        hashMap.put("dbproj", "text/plain");
//        hashMap.put("dcr", "application/x-director");
//        hashMap.put("def", "text/plain");
//        hashMap.put("deploy", "application/octet-stream");
//        hashMap.put("der", "application/x-x509-ca-cert");
//        hashMap.put("dgml", "application/xml");
//        hashMap.put("dib", "image/bmp");
//        hashMap.put("dif", "video/x-dv");
//        hashMap.put("dir", "application/x-director");
//        hashMap.put("disco", "text/xml");
//        hashMap.put("divx", "video/divx");
//        hashMap.put("dll", "application/x-msdownload");
//        hashMap.put("dll.config", "text/xml");
//        hashMap.put("dlm", "text/dlm");
//        hashMap.put("dng", "image/x-adobe-dng");
//        hashMap.put("doc", "application/msword");
//        hashMap.put("docm", "application/vnd.ms-word.document.macroEnabled.12");
//        hashMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
//        hashMap.put("dot", "application/msword");
//        hashMap.put("dotm", "application/vnd.ms-word.template.macroEnabled.12");
//        hashMap.put("dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");
//        hashMap.put("dsp", "application/octet-stream");
//        hashMap.put("dsw", "text/plain");
//        hashMap.put("dtd", "text/xml");
//        hashMap.put("dtsconfig", "text/xml");
//        hashMap.put("dv", "video/x-dv");
//        hashMap.put("dvi", "application/x-dvi");
//        hashMap.put("dwf", "drawing/x-dwf");
//        hashMap.put("dwp", "application/octet-stream");
//        hashMap.put("dxr", "application/x-director");
//        hashMap.put("eml", "message/rfc822");
//        hashMap.put("emz", "application/octet-stream");
//        hashMap.put("eot", "application/vnd.ms-fontobject");
//        hashMap.put("eps", "application/postscript");
//        hashMap.put("etl", "application/etl");
//        hashMap.put("etx", "text/x-setext");
//        hashMap.put("evy", "application/envoy");
//        hashMap.put("exe", "application/octet-stream");
//        hashMap.put("exe.config", "text/xml");
//        hashMap.put("fdf", "application/vnd.fdf");
//        hashMap.put("fif", "application/fractals");
//        hashMap.put("filters", "application/xml");
//        hashMap.put("fla", "application/octet-stream");
//        hashMap.put("flac", "audio/flac");
//        hashMap.put("flr", "x-world/x-vrml");
//        hashMap.put("flv", "video/x-flv");
//        hashMap.put("fsscript", "application/fsharp-script");
//        hashMap.put("fsx", "application/fsharp-script");
//        hashMap.put("generictest", "application/xml");
//        hashMap.put("gif", "image/gif");
//        hashMap.put("group", "text/x-ms-group");
//        hashMap.put("gsm", "audio/x-gsm");
//        hashMap.put("gtar", "application/x-gtar");
//        hashMap.put("gz", "application/x-gzip");
//        hashMap.put("h", "text/plain");
//        hashMap.put("hdf", "application/x-hdf");
//        hashMap.put("hdml", "text/x-hdml");
//        hashMap.put("hhc", "application/x-oleobject");
//        hashMap.put("hhk", "application/octet-stream");
//        hashMap.put("hhp", "application/octet-stream");
//        hashMap.put("hlp", "application/winhlp");
//        hashMap.put("hpp", "text/plain");
//        hashMap.put("hqx", "application/mac-binhex40");
//        hashMap.put("hta", "application/hta");
//        hashMap.put("htc", "text/x-component");
//        hashMap.put("htm", "text/html");
//        hashMap.put("html", "text/html");
//        hashMap.put("htt", "text/webviewhtml");
//        hashMap.put("hxa", "application/xml");
//        hashMap.put("hxc", "application/xml");
//        hashMap.put("hxd", "application/octet-stream");
//        hashMap.put("hxe", "application/xml");
//        hashMap.put("hxf", "application/xml");
//        hashMap.put("hxh", "application/octet-stream");
//        hashMap.put("hxi", "application/octet-stream");
//        hashMap.put("hxk", "application/xml");
//        hashMap.put("hxq", "application/octet-stream");
//        hashMap.put("hxr", "application/octet-stream");
//        hashMap.put("hxs", "application/octet-stream");
//        hashMap.put("hxt", "text/html");
//        hashMap.put("hxv", "application/xml");
//        hashMap.put("hxw", "application/octet-stream");
//        hashMap.put("hxx", "text/plain");
//        hashMap.put("i", "text/plain");
//        hashMap.put("ico", "image/x-icon");
//        hashMap.put("ics", "text/calendar");
//        hashMap.put("idl", "text/plain");
//        hashMap.put("ief", "image/ief");
//        hashMap.put("iii", "application/x-iphone");
//        hashMap.put("inc", "text/plain");
//        hashMap.put("inf", "application/octet-stream");
//        hashMap.put("ini", "text/plain");
//        hashMap.put("inl", "text/plain");
//        hashMap.put("ins", "application/x-internet-signup");
//        hashMap.put("ipa", "application/x-itunes-ipa");
//        hashMap.put("ipg", "application/x-itunes-ipg");
//        hashMap.put("ipproj", "text/plain");
//        hashMap.put("ipsw", "application/x-itunes-ipsw");
//        hashMap.put("iqy", "text/x-ms-iqy");
//        hashMap.put("isp", "application/x-internet-signup");
//        hashMap.put("ite", "application/x-itunes-ite");
//        hashMap.put("itlp", "application/x-itunes-itlp");
//        hashMap.put("itms", "application/x-itunes-itms");
//        hashMap.put("itpc", "application/x-itunes-itpc");
//        hashMap.put("ivf", "video/x-ivf");
//        hashMap.put("jar", "application/java-archive");
//        hashMap.put("java", "application/octet-stream");
//        hashMap.put("jck", "application/liquidmotion");
//        hashMap.put("jcz", "application/liquidmotion");
//        hashMap.put("jfif", "image/pjpeg");
//        hashMap.put("jnlp", "application/x-java-jnlp-file");
//        hashMap.put("jpb", "application/octet-stream");
//        hashMap.put("jpe", "image/jpeg");
//        hashMap.put("jpeg", "image/jpeg");
//        hashMap.put("jpg", "image/jpeg");
//        hashMap.put("js", "application/javascript");
//        hashMap.put("json", "application/json");
//        hashMap.put("jsx", "text/jscript");
//        hashMap.put("jsxbin", "text/plain");
//        hashMap.put("latex", "application/x-latex");
//        hashMap.put("library-ms", "application/windows-library+xml");
//        hashMap.put("lit", "application/x-ms-reader");
//        hashMap.put("loadtest", "application/xml");
//        hashMap.put("lpk", "application/octet-stream");
//        hashMap.put("lsf", "video/x-la-asf");
//        hashMap.put("lst", "text/plain");
//        hashMap.put("lsx", "video/x-la-asf");
//        hashMap.put("lzh", "application/octet-stream");
//        hashMap.put("m13", "application/x-msmediaview");
//        hashMap.put("m14", "application/x-msmediaview");
//        hashMap.put("m1v", "video/mpeg");
//        hashMap.put("m2t", "video/vnd.dlna.mpeg-tts");
//        hashMap.put("m2ts", "video/vnd.dlna.mpeg-tts");
//        hashMap.put("m2v", "video/mpeg");
//        hashMap.put("m3u", "audio/x-mpegurl");
//        hashMap.put("m3u8", "audio/x-mpegurl");
//        hashMap.put("m4a", "audio/m4a");
//        hashMap.put("m4b", "audio/m4b");
//        hashMap.put("m4p", "audio/m4p");
//        hashMap.put("m4r", "audio/x-m4r");
//        hashMap.put("m4v", "video/x-m4v");
//        hashMap.put("mac", "image/x-macpaint");
//        hashMap.put("mak", "text/plain");
//        hashMap.put("man", "application/x-troff-man");
//        hashMap.put("manifest", "application/x-ms-manifest");
//        hashMap.put("map", "text/plain");
//        hashMap.put("master", "application/xml");
//        hashMap.put("mda", "application/msaccess");
//        hashMap.put("mdb", "application/x-msaccess");
//        hashMap.put("mde", "application/msaccess");
//        hashMap.put("mdp", "application/octet-stream");
//        hashMap.put("me", "application/x-troff-me");
//        hashMap.put("mfp", "application/x-shockwave-flash");
//        hashMap.put("mht", "message/rfc822");
//        hashMap.put("mhtml", "message/rfc822");
//        hashMap.put("mid", "audio/mid");
//        hashMap.put("midi", "audio/mid");
//        hashMap.put("mix", "application/octet-stream");
//        hashMap.put("mk", "text/plain");
//        hashMap.put("mkv", "video/x-matroska");
//        hashMap.put("mmf", "application/x-smaf");
//        hashMap.put("mno", "text/xml");
//        hashMap.put("mny", "application/x-msmoney");
//        hashMap.put("mod", "video/mpeg");
//        hashMap.put("mov", "video/quicktime");
//        hashMap.put("movie", "video/x-sgi-movie");
//        hashMap.put("mp2", "video/mpeg");
//        hashMap.put("mp2v", "video/mpeg");
//        hashMap.put("mp3", "audio/mpeg");
//        hashMap.put("mp4", "video/mp4");
//        hashMap.put("mp4v", "video/mp4");
//        hashMap.put("mpa", "video/mpeg");
//        hashMap.put("mpe", "video/mpeg");
//        hashMap.put("mpeg", "video/mpeg");
//        hashMap.put("mpf", "application/vnd.ms-mediapackage");
//        hashMap.put("mpg", "video/mpeg");
//        hashMap.put("mpp", "application/vnd.ms-project");
//        hashMap.put("mpv2", "video/mpeg");
//        hashMap.put("mqv", "video/quicktime");
//        hashMap.put("ms", "application/x-troff-ms");
//        hashMap.put("msi", "application/octet-stream");
//        hashMap.put("mso", "application/octet-stream");
//        hashMap.put("mts", "video/vnd.dlna.mpeg-tts");
//        hashMap.put("mtx", "application/xml");
//        hashMap.put("mvb", "application/x-msmediaview");
//        hashMap.put("mvc", "application/x-miva-compiled");
//        hashMap.put("mxp", "application/x-mmxp");
//        hashMap.put("nc", "application/x-netcdf");
//        hashMap.put("nsc", "video/x-ms-asf");
//        hashMap.put("nws", "message/rfc822");
//        hashMap.put("ocx", "application/octet-stream");
//        hashMap.put("oda", "application/oda");
//        hashMap.put("odb", "application/vnd.oasis.opendocument.database");
//        hashMap.put("odc", "application/vnd.oasis.opendocument.chart");
//        hashMap.put("odf", "application/vnd.oasis.opendocument.formula");
//        hashMap.put("odg", "application/vnd.oasis.opendocument.graphics");
//        hashMap.put("odh", "text/plain");
//        hashMap.put("odi", "application/vnd.oasis.opendocument.image");
//        hashMap.put("odl", "text/plain");
//        hashMap.put("odm", "application/vnd.oasis.opendocument.text-master");
//        hashMap.put("odp", "application/vnd.oasis.opendocument.presentation");
//        hashMap.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
//        hashMap.put("odt", "application/vnd.oasis.opendocument.text");
//        hashMap.put("oga", "audio/ogg");
//        hashMap.put("ogg", "audio/ogg");
//        hashMap.put("ogv", "video/ogg");
//        hashMap.put("ogx", "application/ogg");
//        hashMap.put("one", "application/onenote");
//        hashMap.put("onea", "application/onenote");
//        hashMap.put("onepkg", "application/onenote");
//        hashMap.put("onetmp", "application/onenote");
//        hashMap.put("onetoc", "application/onenote");
//        hashMap.put("onetoc2", "application/onenote");
//        hashMap.put("opus", "audio/ogg");
//        hashMap.put("orderedtest", "application/xml");
//        hashMap.put("osdx", "application/opensearchdescription+xml");
//        hashMap.put("otf", "application/font-sfnt");
//        hashMap.put("otg", "application/vnd.oasis.opendocument.graphics-template");
//        hashMap.put("oth", "application/vnd.oasis.opendocument.text-web");
//        hashMap.put("otp", "application/vnd.oasis.opendocument.presentation-template");
//        hashMap.put("ots", "application/vnd.oasis.opendocument.spreadsheet-template");
//        hashMap.put("ott", "application/vnd.oasis.opendocument.text-template");
//        hashMap.put("oxt", "application/vnd.openofficeorg.extension");
//        hashMap.put("p10", "application/pkcs10");
//        hashMap.put("p12", "application/x-pkcs12");
//        hashMap.put("p7b", "application/x-pkcs7-certificates");
//        hashMap.put("p7c", "application/pkcs7-mime");
//        hashMap.put("p7m", "application/pkcs7-mime");
//        hashMap.put("p7r", "application/x-pkcs7-certreqresp");
//        hashMap.put("p7s", "application/pkcs7-signature");
//        hashMap.put("pbm", "image/x-portable-bitmap");
//        hashMap.put("pcast", "application/x-podcast");
//        hashMap.put("pct", "image/pict");
//        hashMap.put("pcx", "application/octet-stream");
//        hashMap.put("pcz", "application/octet-stream");
//        hashMap.put("pdf", "application/pdf");
//        hashMap.put("pfb", "application/octet-stream");
//        hashMap.put("pfm", "application/octet-stream");
//        hashMap.put("pfx", "application/x-pkcs12");
//        hashMap.put("pgm", "image/x-portable-graymap");
//        hashMap.put("php", "text/plain");
//        hashMap.put("pic", "image/pict");
//        hashMap.put("pict", "image/pict");
//        hashMap.put("pkgdef", "text/plain");
//        hashMap.put("pkgundef", "text/plain");
//        hashMap.put("pko", "application/vnd.ms-pki.pko");
//        hashMap.put("pls", "audio/scpls");
//        hashMap.put("pma", "application/x-perfmon");
//        hashMap.put("pmc", "application/x-perfmon");
//        hashMap.put("pml", "application/x-perfmon");
//        hashMap.put("pmr", "application/x-perfmon");
//        hashMap.put("pmw", "application/x-perfmon");
//        hashMap.put("png", "image/png");
//        hashMap.put("pnm", "image/x-portable-anymap");
//        hashMap.put("pnt", "image/x-macpaint");
//        hashMap.put("pntg", "image/x-macpaint");
//        hashMap.put("pnz", "image/png");
//        hashMap.put("pot", "application/vnd.ms-powerpoint");
//        hashMap.put("potm", "application/vnd.ms-powerpoint.template.macroEnabled.12");
//        hashMap.put("potx", "application/vnd.openxmlformats-officedocument.presentationml.template");
//        hashMap.put("ppa", "application/vnd.ms-powerpoint");
//        hashMap.put("ppam", "application/vnd.ms-powerpoint.addin.macroEnabled.12");
//        hashMap.put("ppm", "image/x-portable-pixmap");
//        hashMap.put("pps", "application/vnd.ms-powerpoint");
//        hashMap.put("ppsm", "application/vnd.ms-powerpoint.slideshow.macroEnabled.12");
//        hashMap.put("ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow");
//        hashMap.put("ppt", "application/vnd.ms-powerpoint");
//        hashMap.put("pptm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
//        hashMap.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
//        hashMap.put("prf", "application/pics-rules");
//        hashMap.put("prm", "application/octet-stream");
//        hashMap.put("prx", "application/octet-stream");
//        hashMap.put("ps", "application/postscript");
//        hashMap.put("psc1", "application/PowerShell");
//        hashMap.put("psd", "application/octet-stream");
//        hashMap.put("psess", "application/xml");
//        hashMap.put("psm", "application/octet-stream");
//        hashMap.put("psp", "application/octet-stream");
//        hashMap.put("pub", "application/x-mspublisher");
//        hashMap.put("pwz", "application/vnd.ms-powerpoint");
//        hashMap.put("py", "text/plain");
//        hashMap.put("qht", "text/x-html-insertion");
//        hashMap.put("qhtm", "text/x-html-insertion");
//        hashMap.put("qt", "video/quicktime");
//        hashMap.put("qti", "image/x-quicktime");
//        hashMap.put("qtif", "image/x-quicktime");
//        hashMap.put("qtl", "application/x-quicktimeplayer");
//        hashMap.put("qxd", "application/octet-stream");
//        hashMap.put("ra", "audio/x-pn-realaudio");
//        hashMap.put("ram", "audio/x-pn-realaudio");
//        hashMap.put("rar", "application/x-rar-compressed");
//        hashMap.put("ras", "image/x-cmu-raster");
//        hashMap.put("rat", "application/rat-file");
//        hashMap.put("rb", "text/plain");
//        hashMap.put("rc", "text/plain");
//        hashMap.put("rc2", "text/plain");
//        hashMap.put("rct", "text/plain");
//        hashMap.put("rdlc", "application/xml");
//        hashMap.put("reg", "text/plain");
//        hashMap.put("resx", "application/xml");
//        hashMap.put("rf", "image/vnd.rn-realflash");
//        hashMap.put("rgb", "image/x-rgb");
//        hashMap.put("rgs", "text/plain");
//        hashMap.put("rm", "application/vnd.rn-realmedia");
//        hashMap.put("rmi", "audio/mid");
//        hashMap.put("rmp", "application/vnd.rn-rn_music_package");
//        hashMap.put("roff", "application/x-troff");
//        hashMap.put("rpm", "audio/x-pn-realaudio-plugin");
//        hashMap.put("rqy", "text/x-ms-rqy");
//        hashMap.put("rtf", "application/rtf");
//        hashMap.put("rtx", "text/richtext");
//        hashMap.put("ruleset", "application/xml");
//        hashMap.put("s", "text/plain");
//        hashMap.put("safariextz", "application/x-safari-safariextz");
//        hashMap.put("scd", "application/x-msschedule");
//        hashMap.put("scr", "text/plain");
//        hashMap.put("sct", "text/scriptlet");
//        hashMap.put("sd2", "audio/x-sd2");
//        hashMap.put("sdp", "application/sdp");
//        hashMap.put("sea", "application/octet-stream");
//        hashMap.put("searchConnector-ms", "application/windows-search-connector+xml");
//        hashMap.put("setpay", "application/set-payment-initiation");
//        hashMap.put("setreg", "application/set-registration-initiation");
//        hashMap.put("settings", "application/xml");
//        hashMap.put("sgimb", "application/x-sgimb");
//        hashMap.put("sgml", "text/sgml");
//        hashMap.put("sh", "application/x-sh");
//        hashMap.put("shar", "application/x-shar");
//        hashMap.put("shtml", "text/html");
//        hashMap.put("sit", "application/x-stuffit");
//        hashMap.put("sitemap", "application/xml");
//        hashMap.put("skin", "application/xml");
//        hashMap.put("sldm", "application/vnd.ms-powerpoint.slide.macroEnabled.12");
//        hashMap.put("sldx", "application/vnd.openxmlformats-officedocument.presentationml.slide");
//        hashMap.put("slk", "application/vnd.ms-excel");
//        hashMap.put("sln", "text/plain");
//        hashMap.put("slupkg-ms", "application/x-ms-license");
//        hashMap.put("smd", "audio/x-smd");
//        hashMap.put("smi", "application/octet-stream");
//        hashMap.put("smx", "audio/x-smd");
//        hashMap.put("smz", "audio/x-smd");
//        hashMap.put("snd", "audio/basic");
//        hashMap.put("snippet", "application/xml");
//        hashMap.put("snp", "application/octet-stream");
//        hashMap.put("sol", "text/plain");
//        hashMap.put("sor", "text/plain");
//        hashMap.put("spc", "application/x-pkcs7-certificates");
//        hashMap.put("spl", "application/futuresplash");
//        hashMap.put("spx", "audio/ogg");
//        hashMap.put("src", "application/x-wais-source");
//        hashMap.put("srf", "text/plain");
//        hashMap.put("ssisdeploymentmanifest", "text/xml");
//        hashMap.put("ssm", "application/streamingmedia");
//        hashMap.put("sst", "application/vnd.ms-pki.certstore");
//        hashMap.put("stl", "application/vnd.ms-pki.stl");
//        hashMap.put("sv4cpio", "application/x-sv4cpio");
//        hashMap.put("sv4crc", "application/x-sv4crc");
//        hashMap.put("svc", "application/xml");
//        hashMap.put("svg", "image/svg+xml");
//        hashMap.put("swf", "application/x-shockwave-flash");
//        hashMap.put("t", "application/x-troff");
//        hashMap.put("tar", "application/x-tar");
//        hashMap.put("tcl", "application/x-tcl");
//        hashMap.put("testrunconfig", "application/xml");
//        hashMap.put("testsettings", "application/xml");
//        hashMap.put("tex", "application/x-tex");
//        hashMap.put("texi", "application/x-texinfo");
//        hashMap.put("texinfo", "application/x-texinfo");
//        hashMap.put("tgz", "application/x-compressed");
//        hashMap.put("thmx", "application/vnd.ms-officetheme");
//        hashMap.put("thn", "application/octet-stream");
//        hashMap.put("tif", "image/tiff");
//        hashMap.put("tiff", "image/tiff");
//        hashMap.put("tlh", "text/plain");
//        hashMap.put("tli", "text/plain");
//        hashMap.put("toc", "application/octet-stream");
//        hashMap.put("tr", "application/x-troff");
//        hashMap.put("trm", "application/x-msterminal");
//        hashMap.put("trx", "application/xml");
//        hashMap.put("ts", "video/vnd.dlna.mpeg-tts");
//        hashMap.put("tsv", "text/tab-separated-values");
//        hashMap.put("ttf", "application/font-sfnt");
//        hashMap.put("tts", "video/vnd.dlna.mpeg-tts");
//        hashMap.put("txt", "text/plain");
//        hashMap.put("u32", "application/octet-stream");
//        hashMap.put("uls", "text/iuls");
//        hashMap.put("user", "text/plain");
//        hashMap.put("ustar", "application/x-ustar");
//        hashMap.put("vb", "text/plain");
//        hashMap.put("vbdproj", "text/plain");
//        hashMap.put("vbk", "video/mpeg");
//        hashMap.put("vbproj", "text/plain");
//        hashMap.put("vbs", "text/vbscript");
//        hashMap.put("vcf", "text/x-vcard");
//        hashMap.put("vcproj", "application/xml");
//        hashMap.put("vcs", "text/calendar");
//        hashMap.put("vcxproj", "application/xml");
//        hashMap.put("vddproj", "text/plain");
//        hashMap.put("vdp", "text/plain");
//        hashMap.put("vdproj", "text/plain");
//        hashMap.put("vdx", "application/vnd.ms-visio.viewer");
//        hashMap.put("vml", "text/xml");
//        hashMap.put("vscontent", "application/xml");
//        hashMap.put("vsct", "text/xml");
//        hashMap.put("vsd", "application/vnd.visio");
//        hashMap.put("vsi", "application/ms-vsi");
//        hashMap.put("vsix", "application/vsix");
//        hashMap.put("vsixlangpack", "text/xml");
//        hashMap.put("vsixmanifest", "text/xml");
//        hashMap.put("vsmdi", "application/xml");
//        hashMap.put("vspscc", "text/plain");
//        hashMap.put("vss", "application/vnd.visio");
//        hashMap.put("vsscc", "text/plain");
//        hashMap.put("vssettings", "text/xml");
//        hashMap.put("vssscc", "text/plain");
//        hashMap.put("vst", "application/vnd.visio");
//        hashMap.put("vstemplate", "text/xml");
//        hashMap.put("vsto", "application/x-ms-vsto");
//        hashMap.put("vsw", "application/vnd.visio");
//        hashMap.put("vsx", "application/vnd.visio");
//        hashMap.put("vtx", "application/vnd.visio");
//        hashMap.put("wav", "audio/wav");
//        hashMap.put("wave", "audio/wav");
//        hashMap.put("wax", "audio/x-ms-wax");
//        hashMap.put("wbk", "application/msword");
//        hashMap.put("wbmp", "image/vnd.wap.wbmp");
//        hashMap.put("wcm", "application/vnd.ms-works");
//        hashMap.put("wdb", "application/vnd.ms-works");
//        hashMap.put("wdp", "image/vnd.ms-photo");
//        hashMap.put("webarchive", "application/x-safari-webarchive");
//        hashMap.put("webm", "video/webm");
//        hashMap.put("webp", "image/webp");
//        hashMap.put("webtest", "application/xml");
//        hashMap.put("wiq", "application/xml");
//        hashMap.put("wiz", "application/msword");
//        hashMap.put("wks", "application/vnd.ms-works");
//        hashMap.put("wlmp", "application/wlmoviemaker");
//        hashMap.put("wlpginstall", "application/x-wlpg-detect");
//        hashMap.put("wlpginstall3", "application/x-wlpg3-detect");
//        hashMap.put("wm", "video/x-ms-wm");
//        hashMap.put("wma", "audio/x-ms-wma");
//        hashMap.put("wmd", "application/x-ms-wmd");
//        hashMap.put("wmf", "application/x-msmetafile");
//        hashMap.put("wml", "text/vnd.wap.wml");
//        hashMap.put("wmlc", "application/vnd.wap.wmlc");
//        hashMap.put("wmls", "text/vnd.wap.wmlscript");
//        hashMap.put("wmlsc", "application/vnd.wap.wmlscriptc");
//        hashMap.put("wmp", "video/x-ms-wmp");
//        hashMap.put("wmv", "video/x-ms-wmv");
//        hashMap.put("wmx", "video/x-ms-wmx");
//        hashMap.put("wmz", "application/x-ms-wmz");
//        hashMap.put("woff", "application/font-woff");
//        hashMap.put("wpl", "application/vnd.ms-wpl");
//        hashMap.put("wps", "application/vnd.ms-works");
//        hashMap.put("wri", "application/x-mswrite");
//        hashMap.put("wrl", "x-world/x-vrml");
//        hashMap.put("wrz", "x-world/x-vrml");
//        hashMap.put("wsc", "text/scriptlet");
//        hashMap.put("wsdl", "text/xml");
//        hashMap.put("wvx", "video/x-ms-wvx");
//        hashMap.put("x", "application/directx");
//        hashMap.put("xaf", "x-world/x-vrml");
//        hashMap.put("xaml", "application/xaml+xml");
//        hashMap.put("xap", "application/x-silverlight-app");
//        hashMap.put("xbap", "application/x-ms-xbap");
//        hashMap.put("xbm", "image/x-xbitmap");
//        hashMap.put("xdr", "text/plain");
//        hashMap.put("xht", "application/xhtml+xml");
//        hashMap.put("xhtml", "application/xhtml+xml");
//        hashMap.put("xla", "application/vnd.ms-excel");
//        hashMap.put("xlam", "application/vnd.ms-excel.addin.macroEnabled.12");
//        hashMap.put("xlc", "application/vnd.ms-excel");
//        hashMap.put("xld", "application/vnd.ms-excel");
//        hashMap.put("xlk", "application/vnd.ms-excel");
//        hashMap.put("xll", "application/vnd.ms-excel");
//        hashMap.put("xlm", "application/vnd.ms-excel");
//        hashMap.put("xls", "application/vnd.ms-excel");
//        hashMap.put("xlsb", "application/vnd.ms-excel.sheet.binary.macroEnabled.12");
//        hashMap.put("xlsm", "application/vnd.ms-excel.sheet.macroEnabled.12");
//        hashMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        hashMap.put("xlt", "application/vnd.ms-excel");
//        hashMap.put("xltm", "application/vnd.ms-excel.template.macroEnabled.12");
//        hashMap.put("xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template");
//        hashMap.put("xlw", "application/vnd.ms-excel");
//        hashMap.put("xml", "text/xml");
//        hashMap.put("xmta", "application/xml");
//        hashMap.put("xof", "x-world/x-vrml");
//        hashMap.put("xoml", "text/plain");
//        hashMap.put("xpm", "image/x-xpixmap");
//        hashMap.put("xps", "application/vnd.ms-xpsdocument");
//        hashMap.put("xrm-ms", "text/xml");
//        hashMap.put("xsc", "application/xml");
//        hashMap.put("xsd", "text/xml");
//        hashMap.put("xsf", "text/xml");
//        hashMap.put("xsl", "text/xml");
//        hashMap.put("xslt", "text/xml");
//        hashMap.put("xsn", "application/octet-stream");
//        hashMap.put("xss", "application/xml");
//        hashMap.put("xspf", "application/xspf+xml");
//        hashMap.put("xtp", "application/octet-stream");
//        hashMap.put("xwd", "image/x-xwindowdump");
//        hashMap.put("z", "application/x-compress");
//        hashMap.put("zip", "application/zip");
//        String lowerCase = getFilenameExtension(str).toLowerCase();
//        String str2 = (String) hashMap.get(lowerCase);
//        return str2 == null ? "" : str2;
//    }
//
//    public String getFilenameExtension(String str) {
//        String substring = str.substring(StringsKt.lastIndexOf((CharSequence) str, ".", 0, false) + 1);
//        return substring;
//    }

}
