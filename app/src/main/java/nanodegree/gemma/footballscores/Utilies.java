package nanodegree.gemma.footballscores;

/**
 * Created by yehya khaled on 3/3/2015.
 * Updated by Gemma Lara 02/2016 for Udacity Android Developer Nanodegree
 */
public class Utilies
{

    public static final int PREMIER_LEGAUE = 398;
    public static final int PRIMERA_DIVISION = 399;

    public static String getLeague(int league_num)
    {
        switch (league_num)
        {

            case PREMIER_LEGAUE : return "Premier League";
            case PRIMERA_DIVISION : return "Primera Division";

            default: return "Not known League Please report";
        }
    }
    public static String getMatchDay(int match_day,int league_num)
    {
//        if(league_num == CHAMPIONS_LEAGUE)
//        {
//            if (match_day <= 6)
//            {
//                return "Group Stages, Matchday : 6";
//            }
//            else if(match_day == 7 || match_day == 8)
//            {
//                return "First Knockout round";
//            }
//            else if(match_day == 9 || match_day == 10)
//            {
//                return "QuarterFinal";
//            }
//            else if(match_day == 11 || match_day == 12)
//            {
//                return "SemiFinal";
//            }
//            else
//            {
//                return "Final";
//            }
//        }
//        else
//        {
            return "Matchday : " + String.valueOf(match_day);
//        }
    }

    public static String getScores(int home_goals,int awaygoals)
    {
        if(home_goals < 0 || awaygoals < 0)
        {
            return " - ";
        }
        else
        {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName (String teamname)
    {
        if (teamname==null){return R.drawable.no_icon;}
        switch (teamname) {
            // GLS added UK league icons
            case "Manchester United FC" : return R.drawable.manchester_united;
            case "Tottenham Hotspur FC" : return R.drawable.tottenham_hotspur;
            case "AFC Bournemouth" : return R.drawable.bournemouth;
            case "Aston Villa FC" : return R.drawable.aston_villa;
            case "Everton FC" : return R.drawable.everton_fc_logo1;
            case "Watford FC" : return R.drawable.watford;
            case "Leicester City FC" : return R.drawable.leicester_city_fc_hd_logo;
            case "Sunderland AFC" : return R.drawable.sunderland;
            case "Norwich City FC" : return R.drawable.norwich_city;
            case "Crystal Palace FC" : return R.drawable.crystal_palace;
            case "Chelsea FC" : return R.drawable.chelsea;
            case "Swansea City FC" : return R.drawable.swansea_city_afc;
            case "Newcastle United FC" : return R.drawable.newcastle_united;
            case "Southampton FC" : return R.drawable.southampton_fc;
            case "Arsenal FC" : return R.drawable.arsenal;
            case "West Ham United FC" : return R.drawable.west_ham;
            case "Stoke City FC" : return R.drawable.stoke_city;
            case "Liverpool FC" : return R.drawable.liverpool;
            case "West Bromwich Albion FC" : return R.drawable.west_bromwich_albion_hd_logo;
            case "Manchester City FC" : return R.drawable.manchester_city;

            // GLS added spanish league icons
            case "RC Deportivo La Coruna": return R.drawable.deportivo_la_coruna;
            case "Real Sociedad de Fútbol": return R.drawable.real_sociedad;
            case "RCD Espanyol" : return R.drawable.espanyol;
            case "Getafe CF" : return R.drawable.getafe;
            case "UD Las Palmas" : return R.drawable.las_palmas;
            case "Rayo Vallecano de Madrid" : return R.drawable.rayo_vallecano;
            case "Valencia CF" : return R.drawable.valencia;
            case "Málaga CF" : return R.drawable.malaga;
            case "Sevilla FC" : return R.drawable.sevilla;
            case "Athletic Club" : return R.drawable.athletic_bilbao;
            case "FC Barcelona" : return R.drawable.barcelona;
            case "Sporting Gijón" : return R.drawable.sporting_gijon;
            case "Real Madrid CF" : return R.drawable.real_madrid;
            case "Levante UD" : return R.drawable.levante;
            case "RC Celta de Vigo" : return R.drawable.celta_vigo;
            case "Real Betis" : return R.drawable.real_betis;
            case "Villarreal CF" : return R.drawable.villareal;
            case "Granada CF" : return R.drawable.granada;
            case "SD Eibar" : return R.drawable.eibar;
            case "Club Atlético de Madrid": return R.drawable.atletico_madrid;


            default: return R.drawable.ic_launcher;
        }
    }
}
