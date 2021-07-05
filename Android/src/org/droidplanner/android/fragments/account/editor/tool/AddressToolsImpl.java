package org.droidplanner.android.fragments.account.editor.tool;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.o3dr.services.android.lib.coordinate.LatLong;
import com.o3dr.services.android.lib.drone.mission.MissionItemType;
import com.o3dr.services.android.lib.drone.mission.item.spatial.BaseSpatialItem;

import org.droidplanner.android.R;

import java.util.Arrays;
import java.util.List;


/**
 * Created by Jeon-Sunghwan on 5/16/21.
 */
class AddressToolsImpl extends EditorToolsImpl implements View.OnClickListener {

    static final MissionItemType[] MARKER_ITEMS_TYPE = {
            MissionItemType.WAYPOINT
            //, MissionItemType.SPLINE_WAYPOINT,
            // MissionItemType.CIRCLE,
            //  MissionItemType.LAND,
            //  MissionItemType.REGION_OF_INTEREST,
            // MissionItemType.STRUCTURE_SCANNER
    };


    private final static String EXTRA_SELECTED_MARKER_MISSION_ITEM_TYPE = "extra_selected_marker_mission_item_type";

    private MissionItemType selectedType = MARKER_ITEMS_TYPE[0];//선택한 타입의 디폴트 값을 웨이포인트


    AddressToolsImpl(EditorToolsFragment fragment) {
        super(fragment);
    }


    @Override
    public EditorToolsFragment.EditorTools getEditorTools() {
        return EditorToolsFragment.EditorTools.COORDINATE;
    }

    @Override
    public void setup() {
        EditorToolsFragment.EditorToolListener listener = editorToolsFragment.listener;
        if (listener != null) {
            listener.enableGestureDetection(false);
        }
        Toast.makeText(editorToolsFragment.getContext(), "Give me a Destination Address",
                Toast.LENGTH_SHORT).show();
        if (missionProxy != null)//미션이 있으면
            missionProxy.selection.clearSelection();
    }


    public void submit() {
        double latValue = editorToolsFragment.lat_address;
        double lotValue = editorToolsFragment.lon_address;
        LatLong text_coordinate = new LatLong(latValue, lotValue);
        BaseSpatialItem spatialItem = (BaseSpatialItem) selectedType.getNewItem();
        missionProxy.addSpatialWaypoint(spatialItem, text_coordinate);

        Toast.makeText(editorToolsFragment.getContext(), "submit Done !",
                Toast.LENGTH_SHORT).show();

    }

    public void AutoCompleteAddress() {
        // Places.initialize(getApplicationContext(),"AIzaSyCQx6M2fQYH0RPnJxdhZxFaxY0TUQWVVOc"); //Places 시작함수 (컨텍스트,API키)

        editorToolsFragment.address_fragment.setFocusable(false); //뭔지 모름

        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                Place.Field.LAT_LNG, Place.Field.NAME); //리스트 객체 생성
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY //Autocomplete(자동완성) 인텐트 생성
                , fieldList).build(editorToolsFragment.getContext());

        Log.d("CatchBug",editorToolsFragment.getActivity().getClass()+"");
        editorToolsFragment.getActivity().startActivityForResult(intent, 100); //인텐트 시작
        //editorToolsFragment.getActivity().setResult(100,intent);

        Toast.makeText(editorToolsFragment.getContext(), "auto complete",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View view) {

        /*
        버튼과 에딧텍스트 분별하기
         */
        switch (view.getId()) {
            case R.id.input_Address: // edit text
                Toast.makeText(editorToolsFragment.getContext(), "address",
                        Toast.LENGTH_SHORT).show();

                AutoCompleteAddress();
                break;
            case R.id.enter_address: // button
                Toast.makeText(editorToolsFragment.getContext(), "enter",
                        Toast.LENGTH_SHORT).show();

                submit();
                break;
        }

        Toast.makeText(editorToolsFragment.getContext(), "Done ! ",
                Toast.LENGTH_SHORT).show();

        // If an mission item is selected, unselect it.
        //  missionProxy.selection.clearSelection(); // real?


    }
}
