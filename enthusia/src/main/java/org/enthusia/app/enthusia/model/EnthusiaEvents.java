package org.enthusia.app.enthusia.model;

import org.enthusia.app.R;

public final class EnthusiaEvents {

    public final static int[] drawables = {
            R.drawable.img_enthusia_cricket,
            R.drawable.img_enthusia_football,
            R.drawable.img_enthusia_basketball,
            R.drawable.img_enthusia_volleyball,
            R.drawable.img_enthusia_rink,
            R.drawable.img_enthusia_tennis,
            R.drawable.img_enthusia_tt,
            R.drawable.img_enthusia_kabaddi,
            R.drawable.img_enthusia_athletics,
            R.drawable.img_enthusia_throwball,
            R.drawable.img_enthusia_squash,
            R.drawable.img_enthusia_boxing,
            R.drawable.img_enthusia_carrom,
            R.drawable.img_enthusia_chess,
            R.drawable.img_enthusia_dodgeball,
            R.drawable.img_enthusia_badminton,
            R.drawable.img_enthusia_khokho,
            R.drawable.img_enthusia_swimming,
            R.drawable.img_enthusia_marathon,
            R.drawable.img_enthusia_handball,
            R.drawable.img_enthusia_cyclothon
    };

    public final static String[] events = {
            "Cricket",
            "Football",
            "Basketball",
            "Volleyball",
            "Rink Football",
            "Tennis",
            "Table Tennis",
            "Kabaddi",
            "Athletics",
            "Throwball",
            "Squash",
            "Boxing",
            "Carrom",
            "Chess",
            "Dodgeball",
            "Badminton",
            "Kho Kho",
            "Swimming",
            "Marathon",
            "Handball",
            "Cyclothon"
    };

    public final static int[] rules = {
            R.string.enthusia_cricket,
            R.string.enthusia_football,
            R.string.enthusia_basketball,
            R.string.enthusia_volleyball,
            R.string.enthusia_rink,
            R.string.enthusia_tennis,
            R.string.enthusia_tt,
            R.string.enthusia_kabaddi,
            R.string.enthusia_athletics,
            R.string.enthusia_throwball,
            R.string.enthusia_squash,
            R.string.enthusia_boxing,
            R.string.enthusia_carrom,
            R.string.enthusia_chess,
            R.string.enthusia_dodgeball,
            R.string.enthusia_badminton,
            R.string.enthusia_kho,
            R.string.enthusia_swimming,
            R.string.enthusia_handball,
            R.string.enthusia_handball,
            R.string.enthusia_handball
    };

    public static String[] getEventHead (int position) {
        switch (position) {
            case 0: // Cricket
                return new String[] { "Anklesh Shekoker: +917387438280", "Smitesh Modak: +919769604373", "Devansh Khaitan: +919167426373", "Shubham Gawande: +918698606142", "Moksh Rathod: +918698606142" };
            case 1: // Football
                return new String[] { "Rohan Amarapurkar: +919819600663", "Aneesh Bhagwat: +919619680824", "Kunal Gajendragadkar: +919769093433", "Pranav Wasnik: +918793712455", "Aditya Chaudhary: +919619651218" };
            case 2: // Basketball
                return new String[] { "Milay Haria: +919029410964", "Mayur Deshmukh: +918408000747", "Viraj Sanghvi: +919923641510", "Dhriti Shetty: +919833418083", "Sakina Tinwala: +919619331995" };
            case 3: // Volleyball
                return new String[] { "Prashant Sanap: +917709402238", "Ajinkya Palwe: +919404323538", "Tejas Katkade: +919975513952", "Rutuja Karampure: +919769951513", "Neha Chavan: +918879820967" };
            case 4: // Rink Football
                return new String[] { "Aayush Awasthi: +919821710930", "Yash Beri: +919833128502", "Riddhish Shah: +919664541489", "Sagar Supe: +919833906926", "Meghana Parab: +918097583693", "Richa Deshmukh: +919930539242" };
            case 5: // Tennis
                return new String[] { "Raghav Agrawal: +919321331848", "Sherin Parmar: +9196192480070", "Prashita Pratapan: +919757341789", "Sharvika Kotre: +919821025567" };
            case 6: // Table Tennis
                return new String[] { "Tejas Sankpal: +918976420889", "Akhil Wasnik: +918087883632", "Arvind Nair: +919167112743", "Omkar Shirtode: +919527270717", "Rajeshri Kalaskar: +919892953520", "Aishwarya Shejwal: +919167186391" };
            case 7: // Kabaddi
                return new String[] { "Sunil Mali: +919699693231", "Mahesh Walde: +919665052253", "Ketan Mhetre: +917758011991" };
            case 8: // Athletics
                return new String[] { "Kshitij Agrawal: +919405470077", "Chinmay Karmalkar: +919820855892", "Deep Gala: +918097157686", "Priya Masne: +918806209812", "Ankita Borhade: +918286334264" };
            case 9: // Throwball
                return new String[] { "Sheetal Kamthewad: +918451869772", "Vritti Rohira: +919821594514" };
            case 10: // Squash
                return new String[] { "Nikhil Shetty: +918097875058", "Akshay Pawar: +917588516680" };
            case 11: // Boxing
                return new String[] { "Nikhil Shetty: +918097875058", "Akshay Pawar: +917588516680" };
            case 12: // Carrom
                return new String[] { "Mohit Jaju: +919665036175", "Satyam Mali: +919503251469", "Ram Paryani: +917385966389", "Vishaka Mundhe: +919404358678" };
            case 13: // Chess
                return new String[] { "Ekansh Mishra: +918652762018", "Satyam Avad: +917588535069", "Pranali Mahajan: +919730110055" };
            case 14: // Dodgeball
                return new String[] { "Sheetal Mhaske: +919665090044", "Swati Dighole: +919768350702" };
            case 15: // Badminton
                return new String[] { "Neeraj Kulkarni: +919702216911", "Jay Doshi: +919619432999", "Shama Kamat: +919930799011", "Bhavna Verma: +919920559575" };
            case 16: // Kho Kho
                return new String[] { "Sanket Giri: +918446977134", "Praful Khobragade: +919892602353", "Kailash Puri: +917776067234", "Akshata Hire: +917208307426", "Pradnya Shivsharan: +918976442036" };
            case 17: // Swimming
                return new String[] { "Abhijit Gupta: +918879433235", "Abraham George: +919619823690", "Ashwini Bhosle: +919029391087" };
            case 18: // Marathon
                return new String[] { "Dhruv Turkhia: +919664016227", "Siddhant Shah: +919930851934", "Manan Doshi: +919167143210", "Harshil Shroff: +919819802193", "Prajeet Thakare: +919769422608" };
            case 19: // Handball
                return new String[] { "Rishikesh: +919987722085" };
            // TODO Add Cyclothon event heads

        }
        return new String[] { "Nikhil Shetty: +918097875058", "Akshay Pawar: +917588516680" };
    }

}
