/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.buggydebugger.bookmyshow3tasks.expandlistview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.buggydebugger.bookmyshow3tasks.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a custom array adapter used to populate the listview whose items will
 * expand to display extra content in addition to the default display.
 */
public class CustomArrayAdapter extends ArrayAdapter<ExpandableListItem> implements Filterable {
    ColorGenerator generator = ColorGenerator.MATERIAL;
    private List<ExpandableListItem> mData=null;
    private PostFilter postFilter;
    private int mLayoutViewResourceId;
    private List<ExpandableListItem> filteredmData=null;

    public CustomArrayAdapter(Context context, int layoutViewResourceId,
                              List<ExpandableListItem> data) {
        super(context, layoutViewResourceId, data);
        mData = data;
        filteredmData=data;
        mLayoutViewResourceId = layoutViewResourceId;
        getFilter();
    }
    @Override
    public int getCount() {
        return filteredmData.size();
    }


    @Override
    public ExpandableListItem getItem(int i) {
        return filteredmData.get(i);
    }

    /**
     * Get user list item id
     * @param i item index
     * @return current item id
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Populates the item in the listview cell with the appropriate data. This method
     * sets the thumbnail image, the title and the extra text. This method also updates
     * the layout parameters of the item's view so that the image and title are centered
     * in the bounds of the collapsed view, and such that the extra text is not displayed
     * in the collapsed state of the cell.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ExpandableListItem object = filteredmData.get(position);

        if(convertView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(mLayoutViewResourceId, parent, false);
        }

        LinearLayout linearLayout = (LinearLayout)(convertView.findViewById(
                R.id.item_linear_layout));
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams
                (AbsListView.LayoutParams.MATCH_PARENT, object.getCollapsedHeight());
        linearLayout.setLayoutParams(linearLayoutParams);

        ImageView imgView = (ImageView)convertView.findViewById(R.id.imageView);
        TextView titleView = (TextView)convertView.findViewById(R.id.tvTitle);
        TextView textView = (TextView)convertView.findViewById(R.id.tvBody);
        TextView idView = (TextView)convertView.findViewById(R.id.tvId);
        titleView.setText(object.getTitle());

        int color = generator.getColor(object.getUserId());
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(Integer.toString(object.getUserId()), color);


        imgView.setImageDrawable(drawable);


//        imgView.setImageBitmap(getCroppedBitmap(BitmapFactory.decodeResource(getContext()
//                .getResources(), object.getImgResource(), null)));
        textView.setText(object.getText());
        idView.setText("#"+object.getId());

        convertView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT));

        ExpandingLayout expandingLayout = (ExpandingLayout)convertView.findViewById(R.id
                .expanding_layout);
        expandingLayout.setExpandedHeight(object.getExpandedHeight());
        expandingLayout.setSizeChangedListener(object);

        if (!object.isExpanded()) {
            expandingLayout.setVisibility(View.GONE);
        } else {
            expandingLayout.setVisibility(View.VISIBLE);
        }

        return convertView;
    }


    @Override
    public PostFilter getFilter() {
        if (postFilter == null) {
            postFilter = new PostFilter();
        }

        return postFilter;
    }

    private class PostFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<ExpandableListItem> tempList = new ArrayList<ExpandableListItem>();

                // search content in friend list
                for (ExpandableListItem post : mData) {
                    if (post.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(post);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = mData.size();
                filterResults.values = mData;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredmData = (ArrayList<ExpandableListItem>) results.values;
            notifyDataSetChanged();
        }
    }



}