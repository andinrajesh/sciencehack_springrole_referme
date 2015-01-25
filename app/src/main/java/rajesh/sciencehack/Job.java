package rajesh.sciencehack;

import java.io.Serializable;

/**
 * Created by user on 24/01/15.
 */
public class Job implements Serializable{
    public String jid, title, company, logo, city, state, country, description, requirements, compensation,
            background_photo, background_photo_mobile, company_url, job_url;
    public int bounty;
    public long created_at;

}
