package com.example.administrator.slopedisplacement.adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.slopedisplacement.R;
import com.example.administrator.slopedisplacement.bean.json.GetSchemeFixedListLogJson;
import com.example.administrator.slopedisplacement.utils.FormatUtils;

import java.util.List;

/**
 * 定点流水数据的适配器
 */

public class FixedFlowDataAdapter extends BaseItemDraggableAdapter<GetSchemeFixedListLogJson.ListBean, BaseViewHolder> {
    private int mTotalCount;

    public FixedFlowDataAdapter(int layoutResId, List<GetSchemeFixedListLogJson.ListBean> data) {
        super(layoutResId, data);
    }

    public void setTotalCount(int totalCount) {
        mTotalCount = totalCount;
    }

    @Override
    protected void convert(BaseViewHolder helper, GetSchemeFixedListLogJson.ListBean item) {
        helper.setText(R.id.tvItemFixedFlowDataId, mTotalCount - helper.getAdapterPosition() + "")
                .setText(R.id.tvItemFixedFlowDataCreateTime, item.getCreateTime() + "")
                .setText(R.id.tvItemFixedFlowName, item.getFixedName() + "")
                .setText(R.id.tvItemFixedFlowDataNowShift, FormatUtils.round(item.getNowShift() * 1000))
                .setText(R.id.tvItemFixedFlowDataShift, FormatUtils.round(item.getShift() * 1000))
                .setText(R.id.tvItemFixedFlowDataAddShift, FormatUtils.round(item.getAddShift() * 1000))
                .setText(R.id.tvItemFixedFlowDataObd, FormatUtils.format3(item.getObd()));
    }
}
