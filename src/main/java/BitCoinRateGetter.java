import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class BitCoinRateGetter {
    public static void main(String[] args) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("https://api.coindesk.com/v1/bpi/currentprice.json");

        try {
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity);

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(responseString);
            String rate = jsonResponse.getJSONObject("bpi").getJSONObject("USD").getString("rate");

            // Convert rate to words
            String rateInWords = convertNumberToWords(rate);

            // Print the rate in words
            System.out.println("Bitcoin rate: " + rateInWords);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String[] units = {
            "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
            "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
    };

    private static final String[] tens = {
            "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    };

    private static String convertNumberToWords(String number) {
        // Remove comma and decimal part from the number
        String cleanedNumber = number.replace(",", "").split("\\.")[0];

        int num = Integer.parseInt(cleanedNumber);

        if (num == 0) {
            return "Zero";
        }

        String words = "";

        if (num < 0) {
            words += "Minus ";
            num = -num;
        }

        if (num < 20) {
            words += units[num];
        } else if (num < 100) {
            words += tens[num / 10] + " " + units[num % 10];
        } else if (num < 1000) {
            words += units[num / 100] + " Hundred " + convertNumberToWords(String.valueOf(num % 100));
        } else if (num < 1000000) {
            words += convertNumberToWords(String.valueOf(num / 1000)) + " Thousand " + convertNumberToWords(String.valueOf(num % 1000));
        } else {
            words += convertNumberToWords(String.valueOf(num / 1000000)) + " Million " + convertNumberToWords(String.valueOf(num % 1000000));
        }

        return words.trim();
    }

}
