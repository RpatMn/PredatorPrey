package HW2;

import java.util.*;

import GenCol.entity;
import GenCol.testGeneral;

public class data extends entity{
public Object key,value,reproductionTime,lifeTime, type;

public data(){
}

public data(Object Key, Object Value, Object ReproductionTime, Object LifeTime, Object Type){
key = Key;
value = Value;
reproductionTime = ReproductionTime;
lifeTime = LifeTime;
type = Type;
}
public String toString(){
return "key = " + key.toString() + " ,value = "+ value.toString();
}
public boolean equals(Object o){
	if (o == this)
	    return true;
Class cl = getClass();
if (!cl.isInstance (o))return false;
data p = (data)o;
return  key.equals(p.key) && value.equals(p.value);
}

public Object getKey(){
return key;
}

public Object getValue(){
return value;
}

public Object getReproductionTime(){
return reproductionTime;
}

public Object getLifeTime(){
return lifeTime;
}

public Object getType(){
return type;
}

public int hashCode(){
return key.hashCode() + value.hashCode() + reproductionTime.hashCode() + lifeTime.hashCode() + type.hashCode();
}

public int compare(Object m,Object n){     //less than
Class cl = getClass();
if (!cl.isInstance (m) || !cl.isInstance(n))return 0;
data pm = (data)m;
data pn = (data)n;
if (m.equals(n))return 0;
if (pm.key.hashCode() < pn.key.hashCode()) return -1;
if (pm.key.hashCode() == pn.key.hashCode()
          && pm.value.hashCode() <= pn.value.hashCode())
                          return -1;
return 1;
}
}
class PairComparator implements Comparator{


public PairComparator(){
}

public boolean equals(Object o){
Class cl = getClass();
if (cl.isInstance (o))return true;
return false;
}

public int compare(Object m,Object n){     //less than
Class cl = testGeneral.getTheClass("GenCol.Pair");
if (!cl.isInstance (m) || !cl.isInstance(n))return 0;
data pm = (data)m;
data pn = (data)n;
if (m.equals(n))return 0;
if (pm.key.hashCode() < pn.key.hashCode()) return -1;
if (pm.key.hashCode() == pn.key.hashCode()
          && pm.value.hashCode() <= pn.value.hashCode())
                          return -1;
return 1;
}
}