��Ҫ��jar�� ��org.apache.httpcomponents.httpclient �汾4.3.3

demon
���ε�¼���÷���:
HttpClientData.getInstance().login(String hotel_id,String secret,String phone);
����ֵ��һ������:https://hlms.yeeuu.com/weixin/?timestamp={timestamp}&nonstr={nonstr}&hotel_id={hotel_id}&sign={sign}&phone={phone}


���·�����Ȩ״̬���÷���:
HttpClientData.getInstance().updateRoom(String room,String hotel_id,String secret,String phone,String start,String end);
����ֵString:true��false  true��ʾ��Ȩ�ɹ� false��Ȩʧ��
ע��: start��end ʱ���תString ����:"1450197009","1458197009"


���������Ȩ״̬���÷���:
HttpClientData.getInstance().deleteRoom(String room, String hotel_id, String secret,String phone);
����ֵString:true��false   true��ʾ����ɹ� false���ʧ��  
ע��:�����ʱ��phone����Ϊ�գ�Ҳ���ǰѸ÷�������������Ȩ�˶����