/**
 * @author (created on 9/18/2017).
 */

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.History;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.utils.IOUtils;

import java.io.File;
import java.util.logging.Logger;


public class Chatbot {

    private static final Logger LOGGER = Logger.getLogger(Chatbot.class.getName());

    private static final boolean TRACE_MODE = true;

    private static final String BOT_NAME = "super";

    public static void main(String[] args) {
        final String resourcesPath = getResourcesPath();
        LOGGER.info(resourcesPath);

        MagicBooleans.trace_mode = TRACE_MODE;
        final Bot bot = new Bot(BOT_NAME, resourcesPath);
        bot.brain.nodeStats();
        final Chat chatSession = new Chat(bot);

        String textLine = "";

        while (true) {
            System.out.print("Human : ");
            textLine = IOUtils.readInputTextLine();
            if ((textLine == null) || (textLine.length() < 1))
                textLine = MagicStrings.null_input;
            if (textLine.equals("q")) {
                LOGGER.warning("Receive command to exit from a chat");
                System.exit(0);
            } else if (textLine.equals("wq")) {
                LOGGER.warning("This will rewrite some data in some files");
                bot.writeQuit();
                System.exit(0);
            } else {
                String request = textLine;
                if (MagicBooleans.trace_mode)
                    LOGGER.warning("STATE=" + request + ":THAT=" + ((History) chatSession.thatHistory.get(0)).get(0) + ":TOPIC=" + chatSession.predicates.get("topic"));
                String response = chatSession.multisentenceRespond(request);
                while (response.contains("&lt;"))
                    response = response.replace("&lt;", "<");
                while (response.contains("&gt;"))
                    response = response.replace("&gt;", ">");
                System.out.println("Robot : " + response);
            }
        }

    }

    private static String getResourcesPath() {
        final File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2); //delete '\.' symbols at the end
        LOGGER.info(path);
        return path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
    }
}