package TQS_HW1.HW1.Services;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import TQS_HW1.HW1.Cache.Cache;
import TQS_HW1.HW1.Exceptions.APINotRespondsException;
import TQS_HW1.HW1.Exceptions.BadRequestException;
import TQS_HW1.HW1.Models.CovidData;
import TQS_HW1.HW1.Models.CovidDataCountry;
import TQS_HW1.HW1.Resolver.CovidDataCountryResolver;
import TQS_HW1.HW1.Resolver.CovidDataResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CovidDataCountryService {    
    private static final Logger log = LoggerFactory.getLogger(CovidDataCountryService.class);

    @Autowired
    CovidDataCountryResolver resolver;

    @Autowired
    CovidDataResolver resolver_all;

    @Autowired
    Cache cache;

    public CovidDataCountry getDataByCountry(String country, String date) throws ParseException, IOException, APINotRespondsException, BadRequestException {
        log.info("Getting Cached Data");
        CovidDataCountry cachedData = cache.getDataByCountry(country, date);
        CovidDataCountry result = null;

        if (cachedData == null) {
            try {
                log.info("Getting Data from the API");  
                result = resolver.getDataByCountry(country, date); 
            } catch (JSONException e) {
                System.err.println(e);
                log.info("Error {}", e.toString());
                return null;
            }

            log.info("Saving Data into cache");  
            cache.saveDataCountry(result);
            return result;
        }

        return cachedData;
    }

    public List<CovidData> getAllData() throws ParseException {
        log.info("Getting All Cached Data");
        List<CovidData> cachedData = cache.getAllData();
        List<CovidData> result = null;

        if (cachedData == null) {
            try {
                log.info("Getting All Data from the API");  
                result = resolver_all.getOverallData();
            } catch (Exception e) {
                System.err.println(e);
                log.info("Error {}", e.toString());
                return null;
            }

            log.info("Saving Data into cache");  
            cache.saveData(result);
            return result;
        }

        System.out.println(cachedData);
        return cachedData;
    }
}
