package fenrirmma.hreysti_app.user.Admin;

/**
 * Created by Notandi on 8.12.2017.
 */

public final class DateConverter {
    private DateConverter(){}

    public static String convert(String x){

        String[] split = x.split(" ");
        String day = split[1];
        String month = split[2];
        String year = split[3];

        month = parseMonth(month);

        StringBuilder sb = new StringBuilder();
        sb.append(day);
        sb.append("/");
        sb.append(month);
        sb.append("/");
        sb.append(year);
        String date = sb.toString();

        return date;
    }

    private static String parseMonth(String m) {
        String _month = null;
        switch (m){
            case "Jan" :
                _month = "01";
                break;
            case "Feb" :
                _month = "02";
                break;
            case "Mar" :
                _month = "03";
                break;
            case "Apr" :
                _month = "04";
                break;
            case "May" :
                _month = "05";
                break;
            case "Jun" :
                _month = "06";
                break;
            case "Jul" :
                _month = "07";
                break;
            case "Aug" :
                _month = "08";
                break;
            case "Sep" :
                _month = "09";
                break;
            case "Okt" :
                _month = "10";
                break;
            case "Nov" :
                _month = "11";
                break;
            case "Dec" :
                _month = "12";
                break;
        }
        return _month;
    }

}