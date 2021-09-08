import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.TranslateRequestInitializer;
import com.google.api.services.translate.model.TranslationsListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TranslateService {
    private static String apiKey = "123445556678788";
    public static Map<String, String> handle(String text, String sourceLanguage, List<String> targetLanguage) {
        Map<String, String> result = new HashMap<>();
        try {
            TranslateRequestInitializer keyInitializer = new TranslateRequestInitializer( apiKey );
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            Translate translate = new Translate.Builder( httpTransport, jsonFactory, null )
                    .setApplicationName( "reviewTranslation" )
                    .setTranslateRequestInitializer( keyInitializer )
                    .build();

            Translate.Translations.List list = translate.new Translations().list(
                    Arrays.asList( text ), "en"
            ).setSource( sourceLanguage ).setKey( apiKey );
            // format : 可防止翻译过程中的换行符、表情符号等被翻译
            list.setFormat( "text" );
            for ( String language : targetLanguage ) {
                TranslationsListResponse response = list.setTarget( language ).execute();
                if ( response.getTranslations().size() > 0 ) {
                    result.put( language, response.getTranslations().get( 0 ).getTranslatedText() );
                }
            }
        } catch ( Exception e ) {
            log.info( "google translate error {}", e.getMessage() );
        }
        return result;
    }
}
