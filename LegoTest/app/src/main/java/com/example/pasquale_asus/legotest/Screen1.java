package com.example.pasquale_asus.legotest;
// you need the following imports in all Java Bridge apps
import com.google.appinventor.components.runtime.BluetoothClient;
import com.google.appinventor.components.runtime.Ev3ColorSensor;
import com.google.appinventor.components.runtime.Ev3GyroSensor;
import com.google.appinventor.components.runtime.Ev3Motors;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.Component;

// import any components you are going to use in your app. In this case, just Button
import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.ListPicker;

// you can use the following header for all apps
public class Screen1 extends Form implements HandlesEventDispatching
{
    // declare all your components as instance variables
    private Button redButton;
    private Ev3Motors motor1;
    private int numbers = 0;
    private HorizontalArrangement hr;
    private ListPicker ListPicker1;
    private Button Button_Disconnect;
    private BluetoothClient BluetoothClient1;
    // $define is where you'll create components, initialize properties and make any calls that
    // you'd put in Screen.Initialize of an App Inventor app
    protected void $define()
    {
        ListPicker1 = new ListPicker(this);
        ListPicker1.FontSize(20);
        ListPicker1.Height(50);
        ListPicker1.Width(LENGTH_FILL_PARENT);
        ListPicker1.Text("Connect");
        Button_Disconnect = new Button(this);
        Button_Disconnect.Enabled(false);
        Button_Disconnect.FontSize(20);
        Button_Disconnect.Height(50);
        Button_Disconnect.Width(LENGTH_FILL_PARENT);
        Button_Disconnect.Text("Disconnect");
        BluetoothClient1 = new BluetoothClient(this);


        hr = new HorizontalArrangement(this);
        hr.BackgroundColor(COLOR_GREEN);
        hr.Width(-1100);
        hr.WidthPercent(120);
        hr.AlignHorizontal(3);

        //setContentView(R.layout.activity_main);
        // create the button component
        redButton = new Button(hr);  // the parameter defines the parent for the component,
        motor1 = new Ev3Motors(this);
        // in this case, "this" the screen.
        // if you had an arrangement, you'd refer to it instead.
        redButton.Text( "Active" );
        redButton.BackgroundColor(COLOR_GREEN );

        // register all events that you want to respond to. The second parameter is
        // just a name, the third is the event you care about and its component-independent
        // so you, e.g., you need just one "Click" event even if many buttons
        EventDispatcher.registerEventForDelegation( this, "RedButtonClick", "Click" );
        EventDispatcher.registerEventForDelegation(this, "AfterPickingEvent", "AfterPicking" );
        EventDispatcher.registerEventForDelegation(this, "ClickEvent", "Click" );
    }
    // dispatchEvent handles all events. You'll use an if-else to identify the component and
    // event and you can either call a method or just respond to the event directly
    // params provides the event parameters (not used here)
    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params )
    {
        if( component.equals(redButton) && eventName.equals("Click") )
        {
            RedButtonClick();
            return true;
        }
        if( component.equals(ListPicker1) && eventName.equals("AfterPicking") ){
            ListPicker1AfterPicking();
            return true;
        }
        if( component.equals(Button_Disconnect) && eventName.equals("Click") ){
            Button_DisconnectClick();
            return true;
        }
        // here is where you'd check for other events of your app...
        return false;

    }
    // this is the event handler that dispatchEvent above calls. We just set the button background
    public void RedButtonClick()
    {
        numbers = numbers+1;
        if(numbers % 2 == 0){
            redButton.Text("Active" + numbers);
            redButton.TextColor(COLOR_WHITE);
            redButton.BackgroundColor(Component.COLOR_BLACK);
        }else{
            redButton.Text("Disable");
            redButton.BackgroundColor(Component.COLOR_RED);
        }
    }

    public void ListPicker1AfterPicking(){
        if(BluetoothClient1.Connect(ListPicker1.Selection())){
            this.Title("Connected");
        }
    }
    public void Button_DisconnectClick(){
        BluetoothClient1.Disconnect();
    }

}