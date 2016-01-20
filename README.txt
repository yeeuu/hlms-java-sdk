需要的jar包 ：org.apache.httpcomponents.httpclient 版本4.3.3

demon
信任登录调用方法:
HttpClientData.getInstance().login(String hotel_id,String secret,String phone);
返回值是一个链接:https://hlms.yeeuu.com/weixin/?timestamp={timestamp}&nonstr={nonstr}&hotel_id={hotel_id}&sign={sign}&phone={phone}


更新房间授权状态调用方法:
HttpClientData.getInstance().updateRoom(String room,String hotel_id,String secret,String phone,String start,String end);
返回值String:true或false  true表示授权成功 false授权失败
注意: start和end 时间戳转String 例如:"1450197009","1458197009"


清除房间授权状态调用方法:
HttpClientData.getInstance().deleteRoom(String room, String hotel_id, String secret,String phone);
返回值String:true或false   true表示清除成功 false清除失败  
注意:清除的时候phone可以为空，也就是把该房间所有所有授权人都清除