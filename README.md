## trackbit
Basic implementation for processing geolocation data sent from a FITBIT device with GPS/companion API capabilities. Technically you can just use the Geolocation data from your phone to do the same thing but the javascript API makes it like 80 million times easier than actually writing an application for your phone to do it. Unless you have android.  

#Barely Cohesive Roadmap
- Finish Postgres backend implementation
- Finish some deserialization nonsense -- Cleanup
- Haversine implemented, add LoC(law of cosines) for fun to track small distance accuracy discrepancies(if any)
- Saner route implementation
- HMAC for endpoints
