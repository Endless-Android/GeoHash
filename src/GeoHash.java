import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

/**
 * Description： Geohash算法就是将经纬度编码，将二维变一维，给地址位置分区的一种算法
 * Author: Eddie
 * Date: Created in 2021/3/8 15:50
 */
public class GeoHash {
    private static final double MINLAT = -90;
    private static final double MAXLAT = 90;
    private static final double MINLNG = -180;
    private static final double MAXLNG = 180;
    private static int numbits = 15; //经纬度单独编码的长度.类似精度

    private static double minLat;
    private static double minLng;

    //Base32编码
    private final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    public GeoHash(){
        setMinLatLng();
    }

    private void setMinLatLng() {
        minLat = MAXLAT - MINLAT;
        for (int i = 0; i < numbits; i++) {
            minLat /= 2.0;
        }
        minLng = MAXLNG - MAXLNG;
        for (int i = 0; i < numbits; i++) {
            minLng /= 2.0;
        }
    }

    public String encode(double lat,double lon){
        BitSet latbits = getBits(lat,-90,90);
        BitSet lonbits = getBits(lon,-180,180);
        StringBuilder sb = new StringBuilder();
        //合并：偶数位放经度，奇数位放纬度
        for (int i = 0; i < numbits; i++) {
            sb.append((lonbits.get(i))?1:0);
            sb.append((latbits.get(i))?1:0);
        }
        String code = base32(Long.parseLong(sb.toString(),2));
        return sb.toString();
    }

    //将经纬度合并后的二进制进行指定的32位编码
    private String base32(long parseLong) {
    char[] buf = new char[65];
    int charPos = 64;
    boolean negative = (parseLong < 0);
    if(!negative){
        parseLong = - parseLong;
    }
    while (parseLong <= -32){
        buf[charPos--] = digits[(int)(-(parseLong % 32))];
        parseLong /= 32;
    }
    buf[charPos--] = digits[(int)(-parseLong)];
    if (negative)
        buf[charPos] = '-';
        return new String(buf,charPos,65-charPos);
    }

    //根据经纬度，转换成对应的二进制
    private BitSet getBits(double lat, double floor, double ceiling) {
        BitSet buffer = new BitSet(numbits);
        for (int i = 0; i < numbits; i++) {
            double mid = (floor + ceiling) / 2;
            if (lat >= mid){
                buffer.set(i); //将指定索引i处的位设置为 true
                floor = mid;
            }else {
                ceiling = mid;
            }

        }
        return buffer;
    }

    public static void main(String[] args) {
        GeoHash geoHash = new GeoHash();
        System.out.println(geoHash.encode(39.928167,116.389550));

    }

}