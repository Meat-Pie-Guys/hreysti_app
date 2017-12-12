package fenrirmma.hreysti_app.Utils;

/**
 * Created by Notandi on 10.12.2017.
 */

public class RandomMotivational {
    private RandomMotivational(){}

    public static String getRandomQuote(){
        String random[] = new String[] {"\"Today I will do what others won’t, so tomorrow I can accomplish what others can’t.\" —Jerry Rice",
                "\"Do something today that your future self will thank you for.\" —Unknown", "\"We are what we repeatedly do. Excellence then is not an act but a habit.\" —Aristotle",
        "\"No matter how slow you go, you are still lapping everybody on the couch.\" —Unknown", "\"Sweat is fat crying.\" —Unknown",
        "\"You miss 100% of the shots you don’t take.\" –Wayne Gretzky", "\"The difference between the impossible and the possible lies in a person’s determination.\" —Tommy Lasorda",
        "\"If you want something you’ve never had, you must be willing to do something you’ve never done.\" —Thomas Jefferson", "\"To give anything less than your best is to sacrifice the gift.\" —Steve Prefontaine",
        "\"You’re only one workout away from a good mood.\" —Unknown", "\"Nothing will work unless you do.\" —Maya Angelou",
        "\"Life begins at the end of your comfort zone.\" —Unknown", "\"I’ve missed more than 9,000 shots in my career. I’ve lost almost 300 games. Twenty-six times I’ve been trusted to take the game-winning shot and missed. I’ve failed over and over and over again in my life. And that is why I succeed.\" —Michael Jordan",
        "\"Strength does not come from physical capacity. It comes from an indomitable will.\" —Mahatma Gandhi", "\"The difference between try and triumph is a little 'umph'.\" —Unknown",
        "\"The last three or four reps is what makes the muscle grow. This area of pain divides the champion from someone else who is not a champion. That’s what most people lack, having the guts to go on and just say they’ll go through the pain no matter what happens.\" —Arnold Schwarzenegger",
        "\"Don’t count the days, make the days count.\" —Muhammad Ali", "\"Making excuses burns zero calories per hour.\" —Unknown"};

        int rand = 1 + (int)(Math.random() * ((18-1) + 1));
        return random[rand];
    }
}