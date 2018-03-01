import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Chandler on 2/13/2018.
 */

public class GetData {
    private double lat;
    private double lon;

    // Extinct and Current Animals
    private Object[][][] namemega2;  //range maps for extinct animals
    private Object[] namemega;    //a list of extinct mammals
    private Object[][][] name2;     //range maps of current animals
    private Object[] name;  //a list of current animals

    //diffusion map
    private Object[][] Dsum;    //map of current nutrient diffusion
    private Object[][] Dsummegaall;     //a map of past nutrient diffusion

    private Object[][][] dismov;    //contains nutrient diffusion capacity of animals>10kg

    public GetData(LatLng coordinates){
        lat = coordinates.latitude;
        lon = coordinates.longitude;
    }

    private void firstPart(){
        //find pixel coordinates using R (raster)
            //[findcoor1, findcoor2] = latlon2pix(R,  lat, long);
        //check if coordinates is on land
            //if LAND(findcoor1, findcoor2) == 1

//      ------------ Extinct Mammals
        //use coordinates to determine which of the 211 range maps have animals in that pixel
            //findextinctan = find(namemega2(findcoor1, findcoor2, :) ~= 0)
        //now find which extinct animals lived in that pixel
            //findextinctan = namemega(findextinct)'

//     ------------ Current Animals
        //-------display current animals > 10kg
        //use coordinates to determine which of the 4746 range maps have animals in that pixel
            //currentanimals = name(findcurrent)'

        //extract data from database (Wiki info)
            //turn list into list of Animals
            //set this list with the Ecosystem class

//      ---------- Nutrient Movement
        //------------ current animals
        //nutrient movement for current animals >10kg
            //returns a matrix
            //currentanimalnutrientmov = squeeze(Dismov(findcoor1, findcoor2, find(name2(findcoor1,
                //findcoor2, :) ~=0)));
        //sum together current
            //totcurrentnutmov = Dsum(findcoor1, findcoor2) + sum(currentanimalnutrientmov);

        //------------ extinct animals
        //nutrient movement for extinct animals
            //pastanimalnutrientmov = squeeze(Dsummega(findcoor1, findcoor2, find(name2(findcoor1,
                //findcoor2, :) ~= 0)));
        //sum together past
            //pastanimalnutrientmovtot = nansum(pastanimalnutrientmov);

        //sum together past and current
            //pastpluscurrent = pastanimalnutrientmovtot + totcurrentnutmov;

        //display past versus current on a bar chart
            //bar([pastpluscurrent totcurrentmov])

        /*
            %have user choose
            %choose current map or past
            %Dsum is a map of current nutrient diffusion
            %Dsummegaall is a map of past nutrient diffusion
            curmap=3;
            if curmap==1
                imagesc(Dsum)
            elseif curmap==2
                imagesc(Dsum+Dsummegaall)
            else
                imagesc(Dsum./(Dsum+Dsummegaall))
            end
         */
    }
}
