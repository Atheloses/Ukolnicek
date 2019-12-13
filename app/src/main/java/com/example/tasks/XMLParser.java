package com.example.tasks;

import android.util.Log;
import android.util.Xml;

import com.example.tasks.ui.tasks.TasksEntry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XMLParser {
    public static String CreateXML(ArrayList<TasksEntry> listOfTasks){
        String output = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<tasks>";

        for (TasksEntry currTask : listOfTasks)
            output+="<task name=\""+currTask.Name+"\" time=\""+currTask.Time.getTime()+"\"/>";

        output += "</tasks>";
        return output;
    }

    private static final String ns = null;

    public static ArrayList<TasksEntry> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private static ArrayList<TasksEntry> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<TasksEntry> entries = new ArrayList<TasksEntry>();

        parser.require(XmlPullParser.START_TAG, ns, "tasks");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("task")) {
                entries.add(readEntry(parser));
            } else {
                parser.next();
            }
        }
        return entries;
    }


    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them
    // off
    // to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.

    private static TasksEntry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "task");

        String name="";
        Date time=null;

        for (int i = 0; i < parser.getAttributeCount(); i++) {

            if (parser.getAttributeName(i).equals("name"))
                name = parser.getAttributeValue(i);

            if (parser.getAttributeName(i).equals("time"))
                time = new Date(Long.parseLong(parser.getAttributeValue(i)));
        }

        parser.next();

        return new TasksEntry(0,name,time);
    }
}
