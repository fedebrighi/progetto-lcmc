package compiler;

import compiler.AST.*;
import compiler.lib.*;

import java.util.HashMap;
import java.util.Map;

public class TypeRels {

    public static Map<String, String> superType = new HashMap<>();    //definisce la gerarchia dei tipi riferimento

	// valuta se il tipo "a" e' <= al tipo "b", dove "a" e "b" sono tipi di base: IntTypeNode o BoolTypeNode
	public static boolean isSubtype(TypeNode a, TypeNode b) {
		return a.getClass().equals(b.getClass()) ||
                ((a instanceof BoolTypeNode) && (b instanceof IntTypeNode)) ||
                ((a instanceof EmptyTypeNode) && (b instanceof RefTypeNode)) ||
                checkRefType(a, b) ||
                checkArrowType(a, b);
	}

    private static boolean checkRefType(TypeNode a, TypeNode b) {
        if (a instanceof RefTypeNode && b instanceof RefTypeNode) {
            RefTypeNode rtn = (RefTypeNode) a;
            if(superType.get(rtn.id) == null) {
                return false;
            } else {
                String superId = superType.get(rtn.id);
                while(!superId.equals(((RefTypeNode) b).id) && superType.containsKey(superId)) {
                    superId = superType.get(superId);
                }
                if(!superType.containsKey(superId) && !superId.equals(((RefTypeNode) b).id)) return false;
                return true;
            }
        } else {
            return false;
        }
    }

    private static boolean checkArrowType(TypeNode a, TypeNode b) {
        if(a instanceof ArrowTypeNode && b instanceof ArrowTypeNode) {
            ArrowTypeNode artnA = (ArrowTypeNode) a;
            ArrowTypeNode artnB = (ArrowTypeNode) b;
            if(artnA.parlist.size() != artnB.parlist.size()) return false;
            for(int i=0; i < artnA.parlist.size(); i++) {
                if(!isSubtype(artnA.parlist.get(i), artnB.parlist.get(i))) return false;
            }
            return isSubtype(artnB.ret,  artnA.ret);
        }
        return false;
    }

}
