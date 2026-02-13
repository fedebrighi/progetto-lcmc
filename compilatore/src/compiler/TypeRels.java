package compiler;

import compiler.AST.*;
import compiler.lib.*;

import java.util.HashMap;
import java.util.Map;

public class TypeRels {

    public static Map<String, String> superType = new HashMap<>();    //definisce la gerarchia dei tipi riferimento

	// valuta se il tipo "a" e' <= al tipo "b", dove "a" e "b" sono tipi di base: IntTypeNode o BoolTypeNode
    public static boolean isSubtype(TypeNode a, TypeNode b) {

        if (a == null || b == null) return false;

        if ((a instanceof EmptyTypeNode) && (b instanceof RefTypeNode)) return true;

        if (a instanceof RefTypeNode && b instanceof RefTypeNode) return checkRefType(a, b);

        if (a instanceof ArrowTypeNode && b instanceof ArrowTypeNode) return checkArrowType(a, b);

        return a.getClass().equals(b.getClass()) || ((a instanceof BoolTypeNode) && (b instanceof IntTypeNode));
    }

    public static TypeNode lowestCommonAncestor(TypeNode a, TypeNode b) {
        if(a instanceof EmptyTypeNode){
            return b;
        } else if(b instanceof EmptyTypeNode){
            return a;
        }
        if(a instanceof RefTypeNode rtnA && b instanceof RefTypeNode rtnB){
            String parentClass = superType.get(rtnA.id);
            while(parentClass != null){
                RefTypeNode rtf = new RefTypeNode(parentClass);
                if(isSubtype(b, rtf)){
                    return rtf;
                }
                parentClass = superType.get(parentClass);
            }
            return null;
        }
        if(a instanceof IntTypeNode || b instanceof IntTypeNode ){
            return new IntTypeNode();
        } else if (a instanceof BoolTypeNode && b instanceof BoolTypeNode) {
            return new BoolTypeNode();
        }
        return null;
    }

    private static boolean checkRefType(TypeNode a, TypeNode b) {
        if (a instanceof RefTypeNode && b instanceof RefTypeNode) {
            RefTypeNode rtn = (RefTypeNode) a;
            if(rtn.id.equals(((RefTypeNode) b).id)) {
                return true;
            }
            String superId = superType.get(rtn.id);
            while(superId != null) {
                if(superId.equals(((RefTypeNode) b).id)) {
                    return true;
                }
                superId = superType.get(superId);
            }
            return false;
        } else {
            return false;
        }
    }

    // Tipo dei parametri controvariante, mentre tipo di output covariante
    private static boolean checkArrowType(TypeNode a, TypeNode b) {
        if(a instanceof ArrowTypeNode && b instanceof ArrowTypeNode) {
            ArrowTypeNode artnA = (ArrowTypeNode) a;
            ArrowTypeNode artnB = (ArrowTypeNode) b;
            if(artnA.parlist.size() != artnB.parlist.size()) return false;
            for(int i=0; i < artnA.parlist.size(); i++) {
                if(!isSubtype(artnB.parlist.get(i), artnA.parlist.get(i))) return false;
            }
            return isSubtype(artnA.ret,  artnB.ret);
        }
        return false;
    }

}
