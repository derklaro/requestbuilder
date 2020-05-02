/*
 * This file is licensed under the MIT License (MIT).
 *
 * Copyright (c) 2019-2020 Pasqual K. <https://derklaro.de>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.derklaro.requestbuilder.types;

import com.github.derklaro.requestbuilder.RequestBuilder;
import com.github.derklaro.requestbuilder.common.Validate;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Represents any mime type which is supported by the {@link RequestBuilder}
 * class.
 * <p>
 * You can set the mime type of the outgoing connection by using:
 *
 * <pre>{@code
 * public static synchronised void main(String... args) {
 *     RequestBuilder builder = RequestBuilder.newBuilder("https://google.de", null).setMimeType(MimeType.getMimeType("application/json"));
 * }
 * }</pre>
 * <p>
 * And to accept only one incoming mime type:
 *
 * <pre>{@code
 * public static synchronised void main(String... args) {
 *     RequestBuilder builder = RequestBuilder.newBuilder("https://google.de", null).accepts(MimeType.getMimeType("application/json"));
 * }
 * }</pre>
 * <p>
 * It's recommended to use {@link MimeTypes#isMimeTypeSupported(String)} to check first if the mime-type
 * is supported before using it directly:
 *
 * <pre>{@code
 * public static synchronised void main(String... args) {
 *     if (MimeTypes.isMimeTypeSupported("application/json")) {
 *         // supported! go on
 *         return;
 *     }
 *
 *     // not supported
 * }
 * }</pre>
 * <p>
 * To get a list of all avilable use {@link MimeTypes#getTypes()}. You can check with this option, if
 * the type is supported, too.
 *
 * @author derklaro, derrop
 * @version RB 1.1
 * @see RequestBuilder#accepts(MimeType)
 * @see RequestBuilder#setMimeType(MimeType)
 * @since RB 1.0.0
 */
public class MimeTypes {

    /**
     * All supported mime types as key to name
     */
    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    /**
     * All supported mime types as object
     */
    private static final Collection<MimeType> TYPES = new ArrayList<>();

    static {
        MIME_TYPES.put("mme", "application/base64");
        MIME_TYPES.put("boo", "application/book");
        MIME_TYPES.put("book", "application/book");
        MIME_TYPES.put("ccad", "application/clariscad");
        MIME_TYPES.put("dp", "application/commonground");
        MIME_TYPES.put("drw", "application/drafting");
        MIME_TYPES.put("xl", "application/excel");
        MIME_TYPES.put("frl", "application/freeloader");
        MIME_TYPES.put("spl", "application/futuresplash");
        MIME_TYPES.put("vew", "application/groupwise");
        MIME_TYPES.put("hta", "application/hta");
        MIME_TYPES.put("unv", "application/i-deas");
        MIME_TYPES.put("inf", "application/inf");
        MIME_TYPES.put("mrc", "application/marc");
        MIME_TYPES.put("mbd", "application/mbedlet");
        MIME_TYPES.put("aps", "application/mime");
        MIME_TYPES.put("ppz", "application/mspowerpoint");
        MIME_TYPES.put("doc", "application/msword");
        MIME_TYPES.put("dot", "application/msword");
        MIME_TYPES.put("w6w", "application/msword");
        MIME_TYPES.put("word", "application/msword");
        MIME_TYPES.put("wiz", "application/msword");
        MIME_TYPES.put("mcp", "application/netmc");
        MIME_TYPES.put("json", "application/json");
        MIME_TYPES.put("www-form", "application/x-www-form-urlencoded");
        MIME_TYPES.put("o", "application/octet-stream");
        MIME_TYPES.put("dump", "application/octet-stream");
        MIME_TYPES.put("exe", "application/octet-stream");
        MIME_TYPES.put("saveme", "application/octet-stream");
        MIME_TYPES.put("arc", "application/octet-stream");
        MIME_TYPES.put("arj", "application/octet-stream");
        MIME_TYPES.put("lhx", "application/octet-stream");
        MIME_TYPES.put("psd", "application/octet-stream");
        MIME_TYPES.put("zoo", "application/octet-stream");
        MIME_TYPES.put("oda", "application/oda");
        MIME_TYPES.put("pdf", "application/pdf");
        MIME_TYPES.put("p7s", "application/pkcs7-signature");
        MIME_TYPES.put("crl", "application/pkix-crl");
        MIME_TYPES.put("ai", "application/postscript");
        MIME_TYPES.put("eps", "application/postscript");
        MIME_TYPES.put("ps", "application/postscript");
        MIME_TYPES.put("prt", "application/pro_eng");
        MIME_TYPES.put("part", "application/pro_eng");
        MIME_TYPES.put("set", "application/set");
        MIME_TYPES.put("smil", "application/smil");
        MIME_TYPES.put("smi", "application/smil");
        MIME_TYPES.put("sol", "application/solids");
        MIME_TYPES.put("sdr", "application/sounder");
        MIME_TYPES.put("stp", "application/step");
        MIME_TYPES.put("step", "application/step");
        MIME_TYPES.put("ssm", "application/streamingmedia");
        MIME_TYPES.put("vda", "application/vda");
        MIME_TYPES.put("fdf", "application/vnd.fdf");
        MIME_TYPES.put("hpg", "application/vnd.hp-hpgl");
        MIME_TYPES.put("hgl", "application/vnd.hp-hpgl");
        MIME_TYPES.put("hpgl", "application/vnd.hp-hpgl");
        MIME_TYPES.put("sst", "application/vnd.ms-pki.certstore");
        MIME_TYPES.put("pko", "application/vnd.ms-pki.pko");
        MIME_TYPES.put("cat", "application/vnd.ms-pki.seccat");
        MIME_TYPES.put("pot", "application/vnd.ms-powerpoint");
        MIME_TYPES.put("ppa", "application/vnd.ms-powerpoint");
        MIME_TYPES.put("pps", "application/vnd.ms-powerpoint");
        MIME_TYPES.put("pwz", "application/vnd.ms-powerpoint");
        MIME_TYPES.put("mpp", "application/vnd.ms-project");
        MIME_TYPES.put("ncm", "application/vnd.nokia.configuration-message");
        MIME_TYPES.put("rng", "application/vnd.nokia.ringing-tone");
        MIME_TYPES.put("rnx", "application/vnd.rn-realplayer");
        MIME_TYPES.put("wmlc", "application/vnd.wap.wmlc");
        MIME_TYPES.put("wmlsc", "application/vnd.wap.wmlscriptc");
        MIME_TYPES.put("web", "application/vnd.xara");
        MIME_TYPES.put("vmd", "application/vocaltec-media-desc");
        MIME_TYPES.put("vmf", "application/vocaltec-media-file");
        MIME_TYPES.put("wp6", "application/wordperfect");
        MIME_TYPES.put("wp", "application/wordperfect");
        MIME_TYPES.put("wp5", "application/wordperfect6.0");
        MIME_TYPES.put("w60", "application/wordperfect6.0");
        MIME_TYPES.put("w61", "application/wordperfect6.1");
        MIME_TYPES.put("wk1", "application/x-123");
        MIME_TYPES.put("aim", "application/x-aim");
        MIME_TYPES.put("aas", "application/x-authorware-seg");
        MIME_TYPES.put("bcpio", "application/x-bcpio");
        MIME_TYPES.put("bsh", "application/x-bsh");
        MIME_TYPES.put("pyc", "application/x-bytecode.python");
        MIME_TYPES.put("bz", "application/x-bzip");
        MIME_TYPES.put("boz", "application/x-bzip2");
        MIME_TYPES.put("bz2", "application/x-bzip2");
        MIME_TYPES.put("vcd", "application/x-cdlink");
        MIME_TYPES.put("cha", "application/x-chat");
        MIME_TYPES.put("chat", "application/x-chat");
        MIME_TYPES.put("cco", "application/x-cocoa");
        MIME_TYPES.put("tgz", "application/x-compressed");
        MIME_TYPES.put("z", "application/x-compressed");
        MIME_TYPES.put("nsc", "application/x-conference");
        MIME_TYPES.put("cpio", "application/x-cpio");
        MIME_TYPES.put("cpt", "application/x-cpt");
        MIME_TYPES.put("deepv", "application/x-deepv");
        MIME_TYPES.put("dir", "application/x-director");
        MIME_TYPES.put("dxr", "application/x-director");
        MIME_TYPES.put("dcr", "application/x-director");
        MIME_TYPES.put("dvi", "application/x-dvi");
        MIME_TYPES.put("elc", "application/x-elc");
        MIME_TYPES.put("env", "application/x-envoy");
        MIME_TYPES.put("evy", "application/x-envoy");
        MIME_TYPES.put("es", "application/x-esrehber");
        MIME_TYPES.put("xlt", "application/x-excel");
        MIME_TYPES.put("xlv", "application/x-excel");
        MIME_TYPES.put("xlc", "application/x-excel");
        MIME_TYPES.put("xlb", "application/x-excel");
        MIME_TYPES.put("xld", "application/x-excel");
        MIME_TYPES.put("xlk", "application/x-excel");
        MIME_TYPES.put("xlm", "application/x-excel");
        MIME_TYPES.put("xll", "application/x-excel");
        MIME_TYPES.put("pre", "application/x-freelance");
        MIME_TYPES.put("gsp", "application/x-gsp");
        MIME_TYPES.put("gss", "application/x-gss");
        MIME_TYPES.put("gtar", "application/x-gtar");
        MIME_TYPES.put("gz", "application/x-gzip");
        MIME_TYPES.put("hdf", "application/x-hdf");
        MIME_TYPES.put("help", "application/x-helpfile");
        MIME_TYPES.put("imap", "application/x-httpd-imap");
        MIME_TYPES.put("ima", "application/x-ima");
        MIME_TYPES.put("ins", "application/x-internett-signup");
        MIME_TYPES.put("iv", "application/x-inventor");
        MIME_TYPES.put("ip", "application/x-ip2");
        MIME_TYPES.put("class", "application/x-java-class");
        MIME_TYPES.put("jcm", "application/x-java-commerce");
        MIME_TYPES.put("skd", "application/x-koan");
        MIME_TYPES.put("skm", "application/x-koan");
        MIME_TYPES.put("skp", "application/x-koan");
        MIME_TYPES.put("skt", "application/x-koan");
        MIME_TYPES.put("latex", "application/x-latex");
        MIME_TYPES.put("ltx", "application/x-latex");
        MIME_TYPES.put("lha", "application/x-lha");
        MIME_TYPES.put("ivy", "application/x-livescreen");
        MIME_TYPES.put("wq1", "application/x-lotus");
        MIME_TYPES.put("lzh", "application/x-lzh");
        MIME_TYPES.put("lzx", "application/x-lzx");
        MIME_TYPES.put("hqx", "application/x-mac-binhex40");
        MIME_TYPES.put("bin", "application/x-macbinary");
        MIME_TYPES.put("mc$", "application/x-magic-cap-package-1.0");
        MIME_TYPES.put("mcd", "application/x-mathcad");
        MIME_TYPES.put("mm", "application/x-meme");
        MIME_TYPES.put("mif", "application/x-mif");
        MIME_TYPES.put("nix", "application/x-mix-transfer");
        MIME_TYPES.put("xlw", "application/x-msexcel");
        MIME_TYPES.put("xla", "application/x-msexcel");
        MIME_TYPES.put("xls", "application/x-msexcel");
        MIME_TYPES.put("ppt", "application/x-mspowerpoint");
        MIME_TYPES.put("ani", "application/x-navi-animation");
        MIME_TYPES.put("nvd", "application/x-navidoc");
        MIME_TYPES.put("map", "application/x-navimap");
        MIME_TYPES.put("stl", "application/x-navistyle");
        MIME_TYPES.put("cdf", "application/x-netcdf");
        MIME_TYPES.put("nc", "application/x-netcdf");
        MIME_TYPES.put("pkg", "application/x-newton-compatible-pkg");
        MIME_TYPES.put("aos", "application/x-nokia-9000-communicator-add-on-software");
        MIME_TYPES.put("omc", "application/x-omc");
        MIME_TYPES.put("omcd", "application/x-omcdatamaker");
        MIME_TYPES.put("omcr", "application/x-omcregerator");
        MIME_TYPES.put("pm4", "application/x-pagemaker");
        MIME_TYPES.put("pm5", "application/x-pagemaker");
        MIME_TYPES.put("pcl", "application/x-pcl");
        MIME_TYPES.put("plx", "application/x-pixclscript");
        MIME_TYPES.put("p10", "application/x-pkcs10");
        MIME_TYPES.put("p12", "application/x-pkcs12");
        MIME_TYPES.put("p7r", "application/x-pkcs7-certreqresp");
        MIME_TYPES.put("p7c", "application/x-pkcs7-mime");
        MIME_TYPES.put("p7m", "application/x-pkcs7-mime");
        MIME_TYPES.put("p7a", "application/x-pkcs7-signature");
        MIME_TYPES.put("mpc", "application/x-project");
        MIME_TYPES.put("mpt", "application/x-project");
        MIME_TYPES.put("mpv", "application/x-project");
        MIME_TYPES.put("mpx", "application/x-project");
        MIME_TYPES.put("wb1", "application/x-qpro");
        MIME_TYPES.put("sdp", "application/x-sdp");
        MIME_TYPES.put("sea", "application/x-sea");
        MIME_TYPES.put("sl", "application/x-seelogo");
        MIME_TYPES.put("shar", "application/x-shar");
        MIME_TYPES.put("swf", "application/x-shockwave-flash");
        MIME_TYPES.put("sprite", "application/x-sprite");
        MIME_TYPES.put("spr", "application/x-sprite");
        MIME_TYPES.put("sit", "application/x-stuffit");
        MIME_TYPES.put("sv4cpio", "application/x-sv4cpio");
        MIME_TYPES.put("sv4crc", "application/x-sv4crc");
        MIME_TYPES.put("tar", "application/x-tar");
        MIME_TYPES.put("tbk", "application/x-tbook");
        MIME_TYPES.put("sbk", "application/x-tbook");
        MIME_TYPES.put("tex", "application/x-tex");
        MIME_TYPES.put("texi", "application/x-texinfo");
        MIME_TYPES.put("texinfo", "application/x-texinfo");
        MIME_TYPES.put("t", "application/x-troff");
        MIME_TYPES.put("tr", "application/x-troff");
        MIME_TYPES.put("roff", "application/x-troff");
        MIME_TYPES.put("man", "application/x-troff-man");
        MIME_TYPES.put("me", "application/x-troff-me");
        MIME_TYPES.put("ms", "application/x-troff-ms");
        MIME_TYPES.put("vsd", "application/x-visio");
        MIME_TYPES.put("vst", "application/x-visio");
        MIME_TYPES.put("vsw", "application/x-visio");
        MIME_TYPES.put("mzz", "application/x-vnd.audioexplosion.mzz");
        MIME_TYPES.put("xpix", "application/x-vnd.ls-xpix");
        MIME_TYPES.put("src", "application/x-wais-source");
        MIME_TYPES.put("wsrc", "application/x-wais-source");
        MIME_TYPES.put("hlp", "application/x-winhelp");
        MIME_TYPES.put("wtk", "application/x-wintalk");
        MIME_TYPES.put("wpd", "application/x-wpwin");
        MIME_TYPES.put("wri", "application/x-wri");
        MIME_TYPES.put("der", "application/x-x509-ca-cert");
        MIME_TYPES.put("cer", "application/x-x509-ca-cert");
        MIME_TYPES.put("crt", "application/x-x509-user-cert");
        MIME_TYPES.put("it", "audio/it");
        MIME_TYPES.put("my", "audio/make");
        MIME_TYPES.put("funk", "audio/make");
        MIME_TYPES.put("pfunk", "audio/make.my.funk");
        MIME_TYPES.put("rmi", "audio/mid");
        MIME_TYPES.put("mpga", "audio/mpeg");
        MIME_TYPES.put("m2a", "audio/mpeg");
        MIME_TYPES.put("s3m", "audio/s3m");
        MIME_TYPES.put("tsi", "audio/tsp-audio");
        MIME_TYPES.put("tsp", "audio/tsplayer");
        MIME_TYPES.put("qcp", "audio/vnd.qcelp");
        MIME_TYPES.put("vox", "audio/voxware");
        MIME_TYPES.put("snd", "audio/x-adpcm");
        MIME_TYPES.put("aif", "audio/x-aiff");
        MIME_TYPES.put("aiff", "audio/x-aiff");
        MIME_TYPES.put("aifc", "audio/x-aiff");
        MIME_TYPES.put("au", "audio/x-au");
        MIME_TYPES.put("gsd", "audio/x-gsm");
        MIME_TYPES.put("gsm", "audio/x-gsm");
        MIME_TYPES.put("jam", "audio/x-jam");
        MIME_TYPES.put("lam", "audio/x-liveaudio");
        MIME_TYPES.put("mod", "audio/x-mod");
        MIME_TYPES.put("m3u", "audio/x-mpequrl");
        MIME_TYPES.put("la", "audio/x-nspaudio");
        MIME_TYPES.put("lma", "audio/x-nspaudio");
        MIME_TYPES.put("ram", "audio/x-pn-realaudio");
        MIME_TYPES.put("rmm", "audio/x-pn-realaudio");
        MIME_TYPES.put("rm", "audio/x-pn-realaudio");
        MIME_TYPES.put("rmp", "audio/x-pn-realaudio-plugin");
        MIME_TYPES.put("rpm", "audio/x-pn-realaudio-plugin");
        MIME_TYPES.put("sid", "audio/x-psid");
        MIME_TYPES.put("ra", "audio/x-realaudio");
        MIME_TYPES.put("vqf", "audio/x-twinvq");
        MIME_TYPES.put("vqe", "audio/x-twinvq-plugin");
        MIME_TYPES.put("vql", "audio/x-twinvq-plugin");
        MIME_TYPES.put("mjf", "audio/x-vnd.audioexplosion.mjuicemediafile");
        MIME_TYPES.put("voc", "audio/x-voc");
        MIME_TYPES.put("wav", "audio/x-wav");
        MIME_TYPES.put("xm", "audio/xm");

        MIME_TYPES.put("pdb", "chemical/x-pdb");
        MIME_TYPES.put("xyz", "chemical/x-pdb");

        MIME_TYPES.put("ivr", "i-world/i-vrml");

        MIME_TYPES.put("bm", "image/bmp");
        MIME_TYPES.put("rast", "image/cmu-raster");
        MIME_TYPES.put("fif", "image/fif");
        MIME_TYPES.put("flo", "image/florian");
        MIME_TYPES.put("turbot", "image/florian");
        MIME_TYPES.put("g3", "image/g3fax");
        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("ief", "image/ief");
        MIME_TYPES.put("iefs", "image/ief");
        MIME_TYPES.put("jfif-tbnl", "image/jpeg");
        MIME_TYPES.put("jut", "image/jutvision");
        MIME_TYPES.put("naplps", "image/naplps");
        MIME_TYPES.put("nap", "image/naplps");
        MIME_TYPES.put("pict", "image/pict");
        MIME_TYPES.put("pic", "image/pict");
        MIME_TYPES.put("jpeg", "image/pjpeg");
        MIME_TYPES.put("jfif", "image/pjpeg");
        MIME_TYPES.put("jpe", "image/pjpeg");
        MIME_TYPES.put("jpg", "image/pjpeg");
        MIME_TYPES.put("x-png", "image/png");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("fpx", "image/vnd.net-fpx");
        MIME_TYPES.put("rf", "image/vnd.rn-realflash");
        MIME_TYPES.put("rp", "image/vnd.rn-realpix");
        MIME_TYPES.put("wbmp", "image/vnd.wap.wbmp");
        MIME_TYPES.put("xif", "image/vnd.xiff");
        MIME_TYPES.put("ras", "image/x-cmu-raster");
        MIME_TYPES.put("dwg", "image/x-dwg");
        MIME_TYPES.put("dxf", "image/x-dwg");
        MIME_TYPES.put("svf", "image/x-dwg");
        MIME_TYPES.put("ico", "image/x-icon");
        MIME_TYPES.put("art", "image/x-jg");
        MIME_TYPES.put("jps", "image/x-jps");
        MIME_TYPES.put("nif", "image/x-niff");
        MIME_TYPES.put("niff", "image/x-niff");
        MIME_TYPES.put("pcx", "image/x-pcx");
        MIME_TYPES.put("pct", "image/x-pict");
        MIME_TYPES.put("pnm", "image/x-portable-anymap");
        MIME_TYPES.put("pbm", "image/x-portable-bitmap");
        MIME_TYPES.put("pgm", "image/x-portable-greymap");
        MIME_TYPES.put("ppm", "image/x-portable-pixmap");
        MIME_TYPES.put("qif", "image/x-quicktime");
        MIME_TYPES.put("qti", "image/x-quicktime");
        MIME_TYPES.put("qtif", "image/x-quicktime");
        MIME_TYPES.put("rgb", "image/x-rgb");
        MIME_TYPES.put("tif", "image/x-tiff");
        MIME_TYPES.put("tiff", "image/x-tiff");
        MIME_TYPES.put("bmp", "image/x-windows-bmp");
        MIME_TYPES.put("xwd", "image/x-xwindowdump");
        MIME_TYPES.put("xbm", "image/xbm");
        MIME_TYPES.put("xpm", "image/xpm");

        MIME_TYPES.put("mht", "message/rfc822");
        MIME_TYPES.put("mhtml", "message/rfc822");

        MIME_TYPES.put("iges", "model/iges");
        MIME_TYPES.put("igs", "model/iges");
        MIME_TYPES.put("dwf", "model/vnd.dwf");
        MIME_TYPES.put("pov", "model/x-pov");

        MIME_TYPES.put("gzip", "multipart/x-gzip");
        MIME_TYPES.put("ustar", "multipart/x-ustar");
        MIME_TYPES.put("zip", "multipart/x-zip");

        MIME_TYPES.put("kar", "music/x-karaoke");

        MIME_TYPES.put("pvu", "paleovu/x-pv");

        MIME_TYPES.put("asp", "text/asp");
        MIME_TYPES.put("css", "text/css");
        MIME_TYPES.put("js", "text/ecmascript");
        MIME_TYPES.put("acgi", "text/html");
        MIME_TYPES.put("htm", "text/html");
        MIME_TYPES.put("htx", "text/html");
        MIME_TYPES.put("html", "text/html");
        MIME_TYPES.put("htmls", "text/html");
        MIME_TYPES.put("mcf", "text/mcf");
        MIME_TYPES.put("pas", "text/pascal");
        MIME_TYPES.put("def", "text/plain");
        MIME_TYPES.put("g", "text/plain");
        MIME_TYPES.put("list", "text/plain");
        MIME_TYPES.put("c++", "text/plain");
        MIME_TYPES.put("text", "text/plain");
        MIME_TYPES.put("mar", "text/plain");
        MIME_TYPES.put("com", "text/plain");
        MIME_TYPES.put("txt", "text/plain");
        MIME_TYPES.put("cxx", "text/plain");
        MIME_TYPES.put("idc", "text/plain");
        MIME_TYPES.put("conf", "text/plain");
        MIME_TYPES.put("log", "text/plain");
        MIME_TYPES.put("sdml", "text/plain");
        MIME_TYPES.put("lst", "text/plain");
        MIME_TYPES.put("rtf", "text/richtext");
        MIME_TYPES.put("rtx", "text/richtext");
        MIME_TYPES.put("wsc", "text/scriplet");
        MIME_TYPES.put("tsv", "text/tab-separated-values");
        MIME_TYPES.put("uris", "text/uri-list");
        MIME_TYPES.put("uni", "text/uri-list");
        MIME_TYPES.put("uri", "text/uri-list");
        MIME_TYPES.put("unis", "text/uri-list");
        MIME_TYPES.put("abc", "text/vnd.abc");
        MIME_TYPES.put("flx", "text/vnd.fmi.flexstor");
        MIME_TYPES.put("rt", "text/vnd.rn-realtext");
        MIME_TYPES.put("wml", "text/vnd.wap.wml");
        MIME_TYPES.put("wmls", "text/vnd.wap.wmlscript");
        MIME_TYPES.put("htt", "text/webviewhtml");
        MIME_TYPES.put("s", "text/x-asm");
        MIME_TYPES.put("asm", "text/x-asm");
        MIME_TYPES.put("aip", "text/x-audiosoft-intra");
        MIME_TYPES.put("cc", "text/x-c");
        MIME_TYPES.put("c", "text/x-c");
        MIME_TYPES.put("cpp", "text/x-c");
        MIME_TYPES.put("htc", "text/x-component");
        MIME_TYPES.put("f", "text/x-fortran");
        MIME_TYPES.put("for", "text/x-fortran");
        MIME_TYPES.put("f77", "text/x-fortran");
        MIME_TYPES.put("f90", "text/x-fortran");
        MIME_TYPES.put("h", "text/x-h");
        MIME_TYPES.put("hh", "text/x-h");
        MIME_TYPES.put("java", "text/x-java-source");
        MIME_TYPES.put("jav", "text/x-java-source");
        MIME_TYPES.put("lsx", "text/x-la-asf");
        MIME_TYPES.put("m", "text/x-m");
        MIME_TYPES.put("p", "text/x-pascal");
        MIME_TYPES.put("hlb", "text/x-script");
        MIME_TYPES.put("csh", "text/x-script.csh");
        MIME_TYPES.put("el", "text/x-script.elisp");
        MIME_TYPES.put("ksh", "text/x-script.ksh");
        MIME_TYPES.put("lsp", "text/x-script.lisp");
        MIME_TYPES.put("pl", "text/x-script.perl");
        MIME_TYPES.put("pm", "text/x-script.perl-module");
        MIME_TYPES.put("py", "text/x-script.phyton");
        MIME_TYPES.put("rexx", "text/x-script.rexx");
        MIME_TYPES.put("sh", "text/x-script.sh");
        MIME_TYPES.put("tcl", "text/x-script.tcl");
        MIME_TYPES.put("tcsh", "text/x-script.tcsh");
        MIME_TYPES.put("zsh", "text/x-script.zsh");
        MIME_TYPES.put("shtml", "text/x-server-parsed-html");
        MIME_TYPES.put("ssi", "text/x-server-parsed-html");
        MIME_TYPES.put("etx", "text/x-setext");
        MIME_TYPES.put("sgm", "text/x-sgml");
        MIME_TYPES.put("sgml", "text/x-sgml");
        MIME_TYPES.put("talk", "text/x-speech");
        MIME_TYPES.put("spc", "text/x-speech");
        MIME_TYPES.put("uil", "text/x-uil");
        MIME_TYPES.put("uue", "text/x-uuencode");
        MIME_TYPES.put("uu", "text/x-uuencode");
        MIME_TYPES.put("vcs", "text/x-vcalendar");
        MIME_TYPES.put("xml", "text/xml");

        MIME_TYPES.put("afl", "video/animaflex");
        MIME_TYPES.put("avs", "video/avs-video");
        MIME_TYPES.put("mpeg", "video/mpeg");
        MIME_TYPES.put("mpa", "video/mpeg");
        MIME_TYPES.put("mpe", "video/mpeg");
        MIME_TYPES.put("mpg", "video/mpeg");
        MIME_TYPES.put("m1v", "video/mpeg");
        MIME_TYPES.put("m2v", "video/mpeg");
        MIME_TYPES.put("qt", "video/quicktime");
        MIME_TYPES.put("mov", "video/quicktime");
        MIME_TYPES.put("moov", "video/quicktime");
        MIME_TYPES.put("vdo", "video/vdo");
        MIME_TYPES.put("rv", "video/vnd.rn-realvideo");
        MIME_TYPES.put("viv", "video/vnd.vivo");
        MIME_TYPES.put("vivo", "video/vnd.vivo");
        MIME_TYPES.put("vos", "video/vosaic");
        MIME_TYPES.put("xdr", "video/x-amt-demorun");
        MIME_TYPES.put("xsr", "video/x-amt-showrun");
        MIME_TYPES.put("fmf", "video/x-atomic3d-feature");
        MIME_TYPES.put("dl", "video/x-dl");
        MIME_TYPES.put("dif", "video/x-dv");
        MIME_TYPES.put("dv", "video/x-dv");
        MIME_TYPES.put("fli", "video/x-fli");
        MIME_TYPES.put("gl", "video/x-gl");
        MIME_TYPES.put("isu", "video/x-isvideo");
        MIME_TYPES.put("mjpg", "video/x-motion-jpeg");
        MIME_TYPES.put("mp3", "video/x-mpeg");
        MIME_TYPES.put("mp2", "video/x-mpeq2a");
        MIME_TYPES.put("asf", "video/x-ms-asf");
        MIME_TYPES.put("asx", "video/x-ms-asf-plugin");
        MIME_TYPES.put("avi", "video/x-msvideo");
        MIME_TYPES.put("qtc", "video/x-qtc");
        MIME_TYPES.put("scm", "video/x-scm");
        MIME_TYPES.put("movie", "video/x-sgi-movie");
        MIME_TYPES.put("mv", "video/x-sgi-movie");

        MIME_TYPES.put("wmf", "windows/metafile");

        MIME_TYPES.put("mime", "www/mime");

        MIME_TYPES.put("ice", "x-conference/x-cooltalk");

        MIME_TYPES.put("mid", "x-music/x-midi");
        MIME_TYPES.put("midi", "x-music/x-midi");

        MIME_TYPES.put("qd3", "x-world/x-3dmf");
        MIME_TYPES.put("qd3d", "x-world/x-3dmf");
        MIME_TYPES.put("svr", "x-world/x-svr");
        MIME_TYPES.put("wrl", "x-world/x-vrml");
        MIME_TYPES.put("wrz", "x-world/x-vrml");
        MIME_TYPES.put("vrml", "x-world/x-vrml");
        MIME_TYPES.put("vrt", "x-world/x-vrt");

        MIME_TYPES.put("xgz", "xgl/drawing");
        MIME_TYPES.put("xmz", "xgl/movie");

        MIME_TYPES.forEach((s, s2) -> TYPES.add(new MimeType(s, s2)));
    }

    /**
     * Gets a mime type from it's key. Check first if the mimetype is supported by using
     * {@link MimeTypes#isMimeTypeSupported(String)}.
     *
     * @param key The key name of the mime type
     * @return The mime type which key equals to the given one.
     * @throws IllegalArgumentException If the mime type cannot be resolved or the key given is null
     * @see MimeTypes#getTypes()
     */
    @Nonnull
    public static MimeType getMimeType(@Nonnull String key) {
        Validate.notNull(key, "Cannot get mime type from null key");

        MimeType mimeType = TYPES.stream().filter(e -> e.getKey().equals(key)).findAny().orElse(null);
        Validate.notNull(mimeType, "Cannot resolve mime type %s", key);

        return mimeType;
    }

    /**
     * Checks if the given mime type exists.
     *
     * @param key The key of the mime type
     * @return If the mime type by the given key exists
     * @throws IllegalArgumentException if the given key is null
     * @since RB 1.0.1
     */
    public static boolean isMimeTypeSupported(@Nonnull String key) {
        Validate.notNull(key, "Cannot get mime type from null key");

        return TYPES.stream().anyMatch(e -> e.getKey().equals(key));
    }

    /**
     * @return All supported mime types
     */
    public static Collection<MimeType> getTypes() {
        return Collections.unmodifiableCollection(TYPES);
    }
}
