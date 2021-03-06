package com.octopus.isp.actions;

import com.octopus.utils.alone.ObjectUtils;
import com.octopus.utils.alone.StringUtils;
import com.octopus.utils.file.FileUtils;
import com.octopus.utils.xml.XMLMakeup;
import com.octopus.utils.xml.XMLObject;
import com.octopus.utils.xml.auto.ResultCheck;
import com.octopus.utils.xml.auto.XMLDoObject;
import com.octopus.utils.xml.auto.XMLParameter;
import com.octopus.utils.xml.desc.Desc;

import java.util.*;

/**
 *
 * Created by Administrator on 2019/4/17.
 */
public class ServiceGuideAction extends XMLDoObject {
    String body;
    Map logicDesc;
    public ServiceGuideAction(XMLMakeup xml, XMLObject parent, Object[] containers) throws Exception {
        super(xml, parent, containers);

        body = FileUtils.getProtocolFile("classpath:com/octopus/isp/actions/templet/ServiceBody.tpl");
        String s = FileUtils.getProtocolFile("classpath:com/octopus/isp/actions/templet/logic.tpl");
        if(StringUtils.isNotBlank(s)) {
            logicDesc = StringUtils.convert2MapJSONObject(s);
        }
    }

    @Override
    public void doInitial() throws Exception {

    }

    @Override
    public boolean checkInput(String xmlid, XMLParameter env, Map input, Map output, Map config) throws Exception {
        return true;
    }

    @Override
    public Object doSomeThing(String xmlid, XMLParameter env, Map input, Map output, Map config) throws Exception {
        if(null != input){
            String op = (String)input.get("op");
            if("getEmptyBody".equals(op)){
                return getEmptyBody();
            }else if("getElements".equals(op)){
                String name = (String) input.get("name");
                if(StringUtils.isNotBlank(name)){
                    if("for".equals(name)){

                    }else if("if".equals(name)){

                    }else if("while".equals(name)){

                    }else if("print".equals(name)){

                    }else if("error".equals(name)){

                    }else if("result".equals(name)){

                    }
                }
                return getLogicElementListAndDesc();
            }else if("getProperties".equals(op)){
                String elementName = (String)input.get("element");
                String action = (String)input.get("action");
                String valuePath = (String)input.get("path");
                if(StringUtils.isBlank(action)) {
                    Map m = new HashMap();
                    if (StringUtils.isNotBlank(elementName)) {
                        Map pros = getElementPropertiesAndDesc(elementName);
                        if (null != pros) {
                            ObjectUtils.appendDeepMapNotReplaceKey(pros, m);
                        }
                    }
                    if (StringUtils.isBlank(elementName) || "do".equals(elementName)) {
                        Map commonPros = getCommonXMLPropertiesAndDesc();
                        if (null != commonPros) {
                            if (m.size() > 0) {
                                ObjectUtils.appendDeepMapNotReplaceKey(commonPros, m);
                            } else {
                                m = commonPros;
                            }
                        }
                    }
                    if(StringUtils.isNotBlank(valuePath)){
                        return ObjectUtils.getValueByPath(m,valuePath);
                    }
                    return m;
                }else{
                    return getElementPropertiesValueDesc(elementName,action,valuePath);
                }

            }else if("getChildren".equals(op)){
                String elementName = (String)input.get("element");
                if(StringUtils.isNotBlank(elementName)) {
                    return getElementChildrenAndDesc(elementName);
                }
            }else if("getValue".equals(op)){
                String elementName = (String)input.get("element");
                String action = (String)input.get("action");
                String valuePath = (String)input.get("path");
                String v = (String)input.get("value");
                if(StringUtils.isBlank(v)) {
                    return getElementPropertiesValue(elementName, action, valuePath);
                }else{
                    return getValue(env,v);
                }
            }else if("getTemplate".equals(op)){
                String name = (String)input.get("name");

            }else if("getDoBody".equals(op)){
                String action = (String)input.get("action");
                return getDoBody(action);
            }else if("getActionList".equals(op)){
                try {
                    XMLDoObject system = (XMLDoObject) getObjectById("system");
                    if(null != system){
                        HashMap in = new HashMap();
                        in.put("op","getServiceInfoList");
                        env.appendParentInputParameter(in);
                        system.doThing(env,null);
                        return env.getResult();
                    }
                }catch (Exception e){
                    log.error("",e);
                }
            }

        }
        return null;

    }
    Map getValue(XMLParameter env,String v){
        if("${actions}".equals(v)){
            return getAllServiceNameAndDesc();
        }else if(v.startsWith("${env}")){
            Object o = ObjectUtils.getValueByPath(env,v);
            if(null != o && o instanceof Map){
                Iterator its = ((Map)o).keySet().iterator();
                HashMap m = new HashMap();
                while(its.hasNext()){
                    String k = (String)its.next();
                    if(k.contains("[")){
                        m.put(k.substring(k.indexOf("[")),"");
                    }else{
                        m.put(k,"");
                    }
                }
                ObjectUtils.sortMapByCompareTo(m);
                return m;

            }
            return null;
        }else if("${methods}".equals(v)){
            return getStringMethodsAndDesc();
        }else if("${structures}".equals(v)){
            return getDataStructureAndDesc();
        }
        return null;
    }

    @Override
    public ResultCheck checkReturn(String xmlid, XMLParameter env, Map input, Map output, Map config, Object ret) throws Exception {
        return new ResultCheck(true,ret);
    }

    @Override
    public boolean commit(String xmlid, XMLParameter env, Map input, Map output, Map config, Object ret) throws Exception {
        return true;
    }

    @Override
    public boolean rollback(String xmlid, XMLParameter env, Map input, Map output, Map config, Object ret, Exception e) throws Exception {
        return true;
    }

    String getNextGuide(String tag , String propertyName,String point){
        if(StringUtils.isBlank(tag) && StringUtils.isBlank(propertyName) && StringUtils.isBlank(point)){
            //get empty body
            return getEmptyBody();
        }else if("action".equals(tag) && "child".equals(point)){
            //return getDoElementList();//include element name and desc
        }else if(StringUtils.isNotBlank(tag) && StringUtils.isBlank(propertyName) && StringUtils.isBlank(point)){
            //get all properties of this tag
            //return getPropertiesOfTag(tag);
        }else if(StringUtils.isNotBlank(tag) && StringUtils.isNotBlank(propertyName) && StringUtils.isBlank(point)){
            //get property

        }
        return null;

    }


    String getEmptyBody(){
        try {
            return body;
        }catch (Exception e){
            log.error("",e);
            return "";
        }
    }

    Map getElementPropertiesAndDesc(String elementName){
        return getChildrenAndDesc("do_element"+"."+elementName+".properties");
    }
    Map getElementChildrenAndDesc(String elementName){
        return getChildrenAndDesc("do_element"+"."+elementName+".children");
    }
    String getDoBody(String name){
        StringBuffer sb = new StringBuffer("<do key=\""+name+"_xx\" action=\""+name+"\"" );
        Map input = (Map)getElementPropertiesValueDesc("do",name,"input");
        if(null != input){
            Map inputd = Desc.getInvokeDescStructure(input);
            if(null != inputd) {
                sb.append(" input=\"").append(ObjectUtils.convertMap2String(inputd,false)).append("\"");
            }
        }
        sb.append("/>");
        return sb.toString();
    }
    Object getElementPropertiesValueDesc(String elementName,String action,String valuePath){
        if("do".equals(elementName) && StringUtils.isNotBlank(action)){
            try {
                XMLObject obj = getObjectById(action);
                Map desc=null;
                if(null !=obj) {
                    desc = obj.getDescStructure();
                }else{
                    XMLDoObject system = (XMLDoObject)getObjectById("system");
                    if(null != system) {
                        Map input = new HashMap();
                        input.put("op","getRemoteDesc");
                        input.put("name",action);
                        desc = (Map)system.doSomeThing("",null,input,null,null);
                    }
                }
                Map pros = (Map)ObjectUtils.getValueByPath(logicDesc,"do_element.do.properties");
                if(null != desc && null !=pros){

                        Map m = new HashMap();
                        ObjectUtils.appendDeepMapNotReplaceKey(pros, m);
                        ObjectUtils.appendDeepMapNotReplaceKey(desc, m);
                        if(StringUtils.isNotBlank(valuePath)) {
                            Object o = ObjectUtils.getValueByPath(m, valuePath);
                            if (null != o && o instanceof Map) {
                                return (Map) o;
                                //return Desc.getParameterDesc((Map)o);
                            }
                        }else{
                            return m;
                        }

                }else if(null != pros){
                    return ObjectUtils.getValueByPath(pros,valuePath);
                }else if(null != desc){

                        return ObjectUtils.getValueByPath(desc, valuePath);

                }
            }catch (Exception e){

            }
        }else {
            return ObjectUtils.getValueByPath(logicDesc,"do_element" + "." + elementName + ".properties." + valuePath);
        }
        return null;
    }

    List getElementPropertiesValue(String elementName,String action,String valuePath){
        if("do".equals(elementName) && StringUtils.isNotBlank(action)){
            XMLObject obj = getObjectById(action);
            Map pros = (Map)ObjectUtils.getValueByPath(logicDesc,"do_element.do.properties");
            if(null != obj && null !=pros){
                try {
                    Map m = new HashMap();
                    ObjectUtils.appendDeepMapNotReplaceKey(pros, m);
                    ObjectUtils.appendDeepMapNotReplaceKey(obj.getDescStructure(), m);

                    Object o = ObjectUtils.getValueByPath(m,valuePath);
                    if(null != o && o instanceof Map) {
                        Object oo = ObjectUtils.getValueByPath((Map)o,valuePath);
                        if(null != oo && oo instanceof Map) {
                            Map dec =  Desc.getParameterDesc((Map)oo);
                            return getValue(dec);
                        }
                    }
                    return null;
                }catch (Exception e){
                    log.error("",e);
                }
            }else {
                Map m = getDesc("do_element" + "." + elementName + ".properties."+valuePath);
                return getValue(m);
            }
        }else {
            Map m = getDesc("do_element" + "." + elementName + ".properties."+valuePath);
            return getValue(m);
        }
        return null;

    }
    List<String> getValue(Map m){
        if (m.containsKey("@value") && StringUtils.isNotBlank(m.get("@value"))) {
            List li = new ArrayList();
            li.add(m.get("@value"));
            return li;
        } else if (m.containsKey("@enum") && StringUtils.isNotBlank(m.get("@enum"))) {
            return (List) m.get("@enum");
        }
        return null;
    }

    List getLogicElementListAndDesc(){
        Map m =  getChildrenAndDesc("do_element");
        List ret = new LinkedList();
        if(null != m){
            Iterator its = m.keySet().iterator();
            while(its.hasNext()){

                String name= (String)its.next();
                Object mt = m.get(name);
                Map pro = getElementPropertiesAndDesc(name);
                String pros = getPropertiesString(name,pro);
                Map cl = getElementChildrenAndDesc(name);
                StringBuffer cls= new StringBuffer();
                if(null != cl ){
                    Iterator is = cl.keySet().iterator();
                    while(is.hasNext()){
                        String k = (String)is.next();
                        Map t = (Map)cl.get(k);
                        cls.append("<"+k+" "+getPropertiesString(k,t)+"></"+k+">");
                    }
                }
                Map n = new HashMap();
                n.put("name",name);
                String desc = "";
                if(null != mt && mt instanceof Map) {
                    n.put("desc", ((Map) mt).get("@desc"));
                    if(null !=n.get("@desc") )
                        desc = n.get("@desc").toString();
                }
                n.put("body","<"+name.trim()+" "+pros+" "+(name.trim().equals("do")?"desc=\"key needs unique ." + desc + "\"":"")+">"+cls.toString()+"</"+name.trim()+">");
                ret.add(n);
            }
        }
        return ret;
    }
    String getPropertiesString(String it,Map pro){
        StringBuffer pros = new StringBuffer();
        if(null != pro && pro.size()>0){
            Iterator im = pro.keySet().iterator();
            while(im.hasNext()){
                Object k = im.next();
                pros.append(" ").append(k).append("=").append("\"").append((null !=pro.get(k) && pro.get(k) instanceof Map)?"{}":"").append("\"");
            }
        }
        return pros.toString();
    }

    Map getCommonXMLPropertiesAndDesc(){
        return getChildrenAndDesc("common_define_properties");
    }

    Map getAllServiceNameAndDesc(){
        Map m = getAllObjects();
        Map ret = new LinkedHashMap();
        Iterator<String> its = m.keySet().iterator();
        while(its.hasNext()){
            try {
                String s = its.next();
                Map o = new HashMap();
                Object desc = ((XMLObject) m.get(s)).getDescStructure().get("desc");
                ret.put(s, desc);
            }catch (Exception e){

            }
        }
        return ret;
    }
    Map getStringMethodsAndDesc(){
        return getChildrenAndDesc("string_methods");
    }
    Map getDataStructureAndDesc(){
        return getChildrenAndDesc("data_structures");
    }

    Map getChildrenAndDesc(String path){
        Object o = ObjectUtils.getValueByPath(logicDesc,path);
        if(null != o && o instanceof Map) {
            Map n = (Map)o;
            Map ret = new LinkedHashMap();
            Iterator<String> its = n.keySet().iterator();
            while(its.hasNext()){
                String k = its.next();
                Map d = getDesc(path+"."+k);
                ret.put(k,d);
            }
            return ret;
        }
        return null;
    }
    Map getDesc(String path){
        Object o = ObjectUtils.getValueByPath(logicDesc,path);
        if(null != o && o instanceof Map) {
            return Desc.getParameterDesc((Map)o);
        }
        return null;
    }

    public static void main(String[] args){
        try{
            ServiceGuideAction s = new ServiceGuideAction(null,null,null);
            Map m = s.getChildrenAndDesc("do_element");
            System.out.println(m);
            System.out.println(s.getElementPropertiesAndDesc("for"));
            System.out.println(s.getElementPropertiesAndDesc("do"));
            System.out.println(s.getElementChildrenAndDesc("if"));
            System.out.println(s.getElementChildrenAndDesc("for"));
            System.out.println(s.getAllServiceNameAndDesc());
            System.out.println(s.getElementPropertiesValueDesc("if",null, "cond"));
            System.out.println(s.getElementPropertiesValue("if", null, "cond"));
            System.out.println(s.getElementPropertiesValue("for", null, "collection"));
            System.out.println();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


