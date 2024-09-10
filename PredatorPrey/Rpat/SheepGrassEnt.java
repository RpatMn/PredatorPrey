package PredatorPrey.Rpat;

import java.util.Comparator;

import GenCol.entity;
import GenCol.testGeneral;

public class SheepGrassEnt extends entity{
public Object key,value,reproductionTime,lifeTime,type, domLifeTime;
public Object isDominant;



public SheepGrassEnt(){
}




public SheepGrassEnt(Object Key, Object Value, Object ReproductionTime, Object LifeTime, Object Type, Object IsDominant ){
key = Key;
value = Value;
reproductionTime = ReproductionTime;
lifeTime = LifeTime;
type = Type;
isDominant = IsDominant;
//domLifeTime=DomLifeTime;

}
public String toString(){
return "key = " + key.toString() + " ,value = "+ value.toString();
}
@SuppressWarnings("rawtypes")
public boolean equals(Object o){
	if (o == this)
	    return true;
Class cl = getClass();
if (!cl.isInstance (o))return false;
SheepGrassEnt p = (SheepGrassEnt)o;
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

public Object isDominant() {
    return isDominant;
}


public int hashCode(){
return key.hashCode() + value.hashCode() + reproductionTime.hashCode() + lifeTime.hashCode() + type.hashCode();
}

@SuppressWarnings("rawtypes")
public int compare(Object m,Object n){     //less than
Class cl = getClass();
if (!cl.isInstance (m) || !cl.isInstance(n))return 0;
SheepGrassEnt pm = (SheepGrassEnt)m;
SheepGrassEnt pn = (SheepGrassEnt)n;
if (m.equals(n))return 0;
if (pm.key.hashCode() < pn.key.hashCode()) return -1;
if (pm.key.hashCode() == pn.key.hashCode()
          && pm.value.hashCode() <= pn.value.hashCode())
                          return -1;
return 1;
}
}
@SuppressWarnings("rawtypes")
class PairComparator implements Comparator{


public PairComparator(){
}

public boolean equals(Object o){
@SuppressWarnings("rawtypes")
Class cl = getClass();
if (cl.isInstance (o))return true;
return false;
}

@SuppressWarnings("rawtypes")
public int compare(Object m,Object n){     //less than
Class cl = testGeneral.getTheClass("GenCol.Pair");
if (!cl.isInstance (m) || !cl.isInstance(n))return 0;
SheepGrassEnt pm = (SheepGrassEnt)m;
SheepGrassEnt pn = (SheepGrassEnt)n;
if (m.equals(n))return 0;
if (pm.key.hashCode() < pn.key.hashCode()) return -1;
if (pm.key.hashCode() == pn.key.hashCode()
          && pm.value.hashCode() <= pn.value.hashCode())
                          return -1;
return 1;
}
}