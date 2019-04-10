/*
 * Javassist, a Java-bytecode translator toolkit.
 * Copyright (C) 1999- Shigeru Chiba. All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License.  Alternatively, the contents of this file may be used under
 * the terms of the GNU Lesser General Public License Version 2.1 or later,
 * or the Apache License Version 2.0.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 */

package com.octopus.utils.cls.javassist.compiler.ast;

import com.octopus.utils.cls.javassist.compiler.CompileError;

/**
 * A node of a a binary tree.  This class provides concrete methods
 * overriding abstract methods in ASTree.
 */
public class Pair extends com.octopus.utils.cls.javassist.compiler.ast.ASTree {
    protected com.octopus.utils.cls.javassist.compiler.ast.ASTree left, right;

    public Pair(com.octopus.utils.cls.javassist.compiler.ast.ASTree _left, com.octopus.utils.cls.javassist.compiler.ast.ASTree _right) {
        left = _left;
        right = _right;
    }

    public void accept(com.octopus.utils.cls.javassist.compiler.ast.Visitor v) throws CompileError { v.atPair(this); }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("(<Pair> ");
        sbuf.append(left == null ? "<null>" : left.toString());
        sbuf.append(" . ");
        sbuf.append(right == null ? "<null>" : right.toString());
        sbuf.append(')');
        return sbuf.toString();
    }

    public com.octopus.utils.cls.javassist.compiler.ast.ASTree getLeft() { return left; }

    public com.octopus.utils.cls.javassist.compiler.ast.ASTree getRight() { return right; }

    public void setLeft(com.octopus.utils.cls.javassist.compiler.ast.ASTree _left) { left = _left; }

    public void setRight(ASTree _right) { right = _right; }
}
