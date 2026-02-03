package compiler;

import java.sql.SQLOutput;
import java.util.*;
import compiler.AST.*;
import compiler.exc.*;
import compiler.lib.*;

public class SymbolTableASTVisitor extends BaseASTVisitor<Void,VoidException> {

	private List<Map<String, STentry>> symTable = new ArrayList<>();
	private Map<String, Map<String, STentry>> classTable = new HashMap<>();
    private int fieldOffset;
    private int methodOffset;
    private int nestingLevel=0; // current nesting level
	private int decOffset=-2; // counter for offset of local declarations at current nesting level 
	private int classOffset=-2;
    int stErrors=0;

	SymbolTableASTVisitor() {}
	SymbolTableASTVisitor(boolean debug) {super(debug);} // enables print for debugging

	private STentry stLookup(String id) {
		int j = nestingLevel;
		STentry entry = null;
		while (j >= 0 && entry == null) 
			entry = symTable.get(j--).get(id);	
		return entry;
	}

	@Override
	public Void visitNode(ProgLetInNode n) {
		if (print) printNode(n);
		Map<String, STentry> hm = new HashMap<>();
		symTable.add(hm);
	    for (Node dec : n.declist) visit(dec);
		visit(n.exp);
		symTable.remove(0);
		return null;
	}

	@Override
	public Void visitNode(ProgNode n) {
		if (print) printNode(n);
		visit(n.exp);
		return null;
	}
	
	@Override
	public Void visitNode(FunNode n) {
		if (print) printNode(n);
		Map<String, STentry> hm = symTable.get(nestingLevel);
		List<TypeNode> parTypes = new ArrayList<>();  
		for (ParNode par : n.parlist) parTypes.add(par.getType()); 
		STentry entry = new STentry(nestingLevel, new ArrowTypeNode(parTypes,n.retType),decOffset--);
		//inserimento di ID nella symtable
		if (hm.put(n.id, entry) != null) {
			System.out.println("Fun id " + n.id + " at line "+ n.getLine() +" already declared");
			stErrors++;
		} 
		//creare una nuova hashmap per la symTable
		nestingLevel++;
		Map<String, STentry> hmn = new HashMap<>();
		symTable.add(hmn);
		int prevNLDecOffset=decOffset; // stores counter for offset of declarations at previous nesting level 
		decOffset=-2;
		
		int parOffset=1;
		for (ParNode par : n.parlist)
			if (hmn.put(par.id, new STentry(nestingLevel,par.getType(),parOffset++)) != null) {
				System.out.println("Par id " + par.id + " at line "+ n.getLine() +" already declared");
				stErrors++;
			}
		for (Node dec : n.declist) visit(dec);
		visit(n.exp);
		//rimuovere la hashmap corrente poiche' esco dallo scope               
		symTable.remove(nestingLevel--);
		decOffset=prevNLDecOffset; // restores counter for offset of declarations at previous nesting level 
		return null;
	}
	
	@Override
	public Void visitNode(VarNode n) {
		if (print) printNode(n);
		visit(n.exp);
		Map<String, STentry> hm = symTable.get(nestingLevel);
		STentry entry = new STentry(nestingLevel,n.getType(),decOffset--);
		//inserimento di ID nella symtable
		if (hm.put(n.id, entry) != null) {
			System.out.println("Var id " + n.id + " at line "+ n.getLine() +" already declared");
			stErrors++;
		}
		return null;
	}

	@Override
	public Void visitNode(PrintNode n) {
		if (print) printNode(n);
		visit(n.exp);
		return null;
	}

	@Override
	public Void visitNode(IfNode n) {
		if (print) printNode(n);
		visit(n.cond);
		visit(n.th);
		visit(n.el);
		return null;
	}
	
	@Override
	public Void visitNode(EqualNode n) {
		if (print) printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}

	@Override
	public Void visitNode(LessEqualNode n) {
		if (print) printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}

	@Override
	public Void visitNode(GreaterEqualNode n) {
		if (print) printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}

	@Override
	public Void visitNode(AndNode n) {
		if (print) printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}

	@Override
	public Void visitNode(OrNode n) {
		if (print) printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}

	@Override
	public Void visitNode(NotNode n) {
		if (print) printNode(n);
		visit(n.exp);
		return null;
	}
	
	@Override
	public Void visitNode(TimesNode n) {
		if (print) printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}

	@Override
	public Void visitNode(DivNode n) {
		if (print) printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}
	
	@Override
	public Void visitNode(PlusNode n) {
		if (print) printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}

	@Override
	public Void visitNode(MinusNode n) {
		if (print) printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}

	@Override
	public Void visitNode(CallNode n) {
		if (print) printNode(n);
		STentry entry = stLookup(n.id);
		if (entry == null) {
			System.out.println("Fun id " + n.id + " at line "+ n.getLine() + " not declared");
			stErrors++;
		} else {
			n.entry = entry;
			n.nl = nestingLevel;
		}
		for (Node arg : n.arglist) visit(arg);
		return null;
	}

	@Override
	public Void visitNode(IdNode n) {
		if (print) printNode(n);
        for (int i = 0; i < symTable.size(); i++){
            Map<String, STentry> hm = symTable.get(i);
            for (String id : hm.keySet()) {
                System.out.println(id);
            }
        }
		STentry entry = stLookup(n.id);
		if (entry == null) {
			System.out.println("Var or Par id " + n.id + " at line "+ n.getLine() + " not declared");
			stErrors++;
		} else {
			n.entry = entry;
			n.nl = nestingLevel;
		}
		return null;
	}

	@Override
	public Void visitNode(BoolNode n) {
		if (print) printNode(n, n.val.toString());
		return null;
	}

	@Override
	public Void visitNode(IntNode n) {
		if (print) printNode(n, n.val.toString());
		return null;
	}

    @Override
    public Void visitNode(ClassNode n){
        if (print) printNode(n);
        Map<String, STentry> globalLevel = symTable.get(0);
        ClassTypeNode ctn = new ClassTypeNode(new ArrayList<>(), new ArrayList<>());
        STentry classEntry = new STentry(nestingLevel, ctn,  classOffset--);
        if(globalLevel.put(n.id,  classEntry) != null) {
            System.out.println("Class id " + n.id + " at line "+ n.getLine() +" already declared");
            stErrors++;
        }
        Map<String, STentry> vt = new HashMap<>();
        classTable.put(n.id, vt);


        nestingLevel++;
        symTable.add(vt);

        fieldOffset = -1;
        int prevFieldOffest = fieldOffset;
        for(FieldNode f : n.fields) {
            if (classTable.get(n.id).put(f.id, new STentry(nestingLevel,f.getType(),fieldOffset)) != null) {
                System.out.println("Field id " + f.id + " at line "+ f.getLine() +" already declared");
                stErrors++;
            }
            f.offset = fieldOffset;
            int pos = -fieldOffset - 1;
            while (ctn.allFields.size() <= pos) {
                ctn.allFields.add(null);
            }
            ctn.allFields.set(pos ,f.getType());
            fieldOffset--;
        }
        methodOffset = 0;
        int prevMethodOffest = methodOffset;
        for(MethodNode m : n.methods) {
            visit(m);
            STentry mEntry = symTable.get(nestingLevel).get(m.id);
            if (mEntry != null) {
                if (mEntry.type instanceof ArrowTypeNode atn) {
                    while (ctn.allMethods.size() <= m.offset) {
                        ctn.allMethods.add(null);
                    }
                    ctn.allMethods.set(m.offset, atn);
                } else {
                    System.out.println("Method id " + m.id + " at line " + m.getLine()
                            + " has non-function type in symbol table");
                    stErrors++;
                }
            }
        }
        symTable.remove(nestingLevel--);
        fieldOffset = prevFieldOffest;
        methodOffset = prevMethodOffest;

        return null;
    }

    @Override
    public Void visitNode(MethodNode n) {
        if (print) printNode(n);
        Map<String, STentry> hm = symTable.get(nestingLevel);
        List<TypeNode> parTypes = new ArrayList<>();
        for (ParNode par : n.parlist) parTypes.add(par.getType());
        ArrowTypeNode methodType = new ArrowTypeNode(parTypes,n.retType);
        STentry entry = new STentry(nestingLevel, methodType,methodOffset);
        if (hm.put(n.id, entry) != null) {
            System.out.println("Method id " + n.id + " at line "+ n.getLine() +" already declared");
            stErrors++;
        }
        n.offset = methodOffset++;
        nestingLevel++;
        Map<String, STentry> hmn = new HashMap<>();
        symTable.add(hmn);
        int prevNLDecOffset=decOffset;
        decOffset=-2;

        int parOffset=1;
        for (ParNode par : n.parlist)
            if (hmn.put(par.id, new STentry(nestingLevel,par.getType(),parOffset++)) != null) {
                System.out.println("Par id " + par.id + " at line "+ n.getLine() +" already declared");
                stErrors++;
            }
        for (Node dec : n.declist) visit(dec);
        visit(n.exp);
        symTable.remove(nestingLevel--);
        decOffset=prevNLDecOffset;
        return null;
    }

    @Override
    public Void visitNode(ClassCallNode n) {
        if (print) printNode(n);
        STentry objectEntry = stLookup(n.objectId);
        if (objectEntry == null) {
            System.out.println("Obj id " + n.objectId + " at line "+ n.getLine() + " not declared");
            stErrors++;
        } else {
            n.entry = objectEntry;
            n.nl = nestingLevel;
        }
        if(objectEntry.type instanceof RefTypeNode rtn) {
            STentry methodEntry = classTable.get(rtn.id).get(n.methodId);
            if (methodEntry == null) {
                System.out.println("Method id " + n.methodId + " at line "+ n.getLine() + " not declared");
                stErrors++;
            } else {
                n.methodEntry = methodEntry;
                n.nl = nestingLevel;
            }
        } else {
            System.out.println("Type id " + n.objectId + " at line "+ n.getLine() + " not declared");
            stErrors++;
        }

        return null;
    }

    @Override
    public Void visitNode(NewNode n) {
        if (print) printNode(n);
        System.out.println(classTable.entrySet());
        if(classTable.get(n.id) == null) {
            System.out.println("Class id " + n.id + " at line "+ n.getLine() + " not declared");
            stErrors++;
        }
        n.entry = symTable.get(0).get(n.id);
        return null;
    }

    @Override
    public Void visitNode(RefTypeNode n) {
        if (print) printNode(n);
        return null;
    }

    @Override
    public Void visitNode(EmptyTypeNode n) {
        if (print) printNode(n);
        return null;
    }
}
