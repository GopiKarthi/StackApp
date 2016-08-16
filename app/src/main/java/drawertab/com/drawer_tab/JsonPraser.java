package drawertab.com.drawer_tab;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gopinath.munusamy on 8/14/2016.
 */
public class JsonPraser {
    public ArrayList<Questions> JsonPraser(JSONArray ja){
        String tags,reputation,user_id,user_type,accept_rate,profile_image,display_name,owner_link,is_answered,view_count
                ,protected_date,accepted_answer_id,answer_count,score,last_activity_date,creation_date,last_edit_date,question_id,link,title;

        ArrayList<Questions> qlist = new ArrayList<>();

        for(int i=0; i < ja.length(); i++){

            tags = null;
            reputation = null;
            user_id = null;
            user_type = null;
            accept_rate = null;
            profile_image = null;
            display_name = null;
            owner_link = null;
            is_answered = null;
            view_count = null;
            protected_date = null;
            accepted_answer_id = null;
            answer_count = null;
            score = null;
            last_activity_date = null;
            creation_date = null;
            last_edit_date = null;
            question_id = null;
            link = null;
            title = null;
            JSONObject jsonObject = null;
            try {
                jsonObject = ja.getJSONObject(i);
            JSONObject owner_obj = jsonObject.getJSONObject("owner");

            if(owner_obj.has("reputation")) reputation = owner_obj.getString("reputation");
            Log.i("reputation", reputation, null);
            if(owner_obj.has("user_id")) user_id = owner_obj.getString("user_id");
            Log.i("user_id", user_id, null);
            if(owner_obj.has("user_type")) user_type = owner_obj.getString("user_type");
            Log.i("user_type", user_type, null);
            if(owner_obj.has("accept_rate")) accept_rate = owner_obj.getString("accept_rate");
            Log.i("accept_rate", accept_rate, null);
            if(owner_obj.has("profile_image")) profile_image = owner_obj.getString("profile_image");
            Log.i("profile_image", profile_image, null);
            if(owner_obj.has("display_name")) display_name = owner_obj.getString("display_name");
            Log.i("display_name", display_name, null);
            if(owner_obj.has("link")) owner_link = owner_obj.getString("link");
            Log.i("owner_link", owner_link, null);

            if(jsonObject.has("tags")) tags = jsonObject.getString("tags").replace('"', ' ').replace("[","").replace("]","").replace(",", "");
            Log.i("tags", tags, null);
            if(jsonObject.has("is_answered")) is_answered = jsonObject.getString("is_answered");
            Log.i("is_answered", is_answered, null);
            if(jsonObject.has("view_count")) view_count = jsonObject.getString("view_count");
            Log.i("view_count", view_count, null);
            if(jsonObject.has("protected_date")) protected_date = jsonObject.getString("protected_date");
            Log.i("protected_date", protected_date, null);
            if(jsonObject.has("accepted_answer_id")) accepted_answer_id = jsonObject.getString("accepted_answer_id");
            Log.i("accepted_answer_id", accepted_answer_id, null);
            if(jsonObject.has("answer_count")) answer_count = jsonObject.getString("answer_count");
            Log.i("answer_count", answer_count, null);
            if(jsonObject.has("score")) score = jsonObject.getString("score");
            Log.i("score", score, null);
            if(jsonObject.has("last_activity_date")) last_activity_date = jsonObject.getString("last_activity_date");
            Log.i("last_activity_date", last_activity_date, null);
            if(jsonObject.has("creation_date")) creation_date = jsonObject.getString("creation_date");
            Log.i("creation_date", creation_date, null);
            if(jsonObject.has("last_edit_date")) last_edit_date = jsonObject.getString("last_edit_date");
            Log.i("last_edit_date", last_edit_date, null);
            if(jsonObject.has("question_id")) question_id = jsonObject.getString("question_id");
            Log.i("question_id", question_id, null);
            if(jsonObject.has("link")) link = jsonObject.getString("link");
            Log.i("link", link, null);
            if(jsonObject.has("title")) title = jsonObject.getString("title");
            Log.i("title", title, null);
            Questions q = new Questions(tags,  reputation,  user_id,  user_type,  accept_rate,
                    profile_image,  display_name,  owner_link,  is_answered,  view_count,  protected_date,
                    accepted_answer_id,  answer_count,  score,  last_activity_date,  creation_date,  last_edit_date,
                    question_id,  link,  title);
            qlist.add(q);

            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
        return qlist;
    }
}
