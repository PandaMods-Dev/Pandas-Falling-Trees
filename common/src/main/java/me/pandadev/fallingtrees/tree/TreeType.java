package me.pandadev.fallingtrees.tree;

import java.util.concurrent.locks.Condition;

public class TreeType {
	public final TreeCondition conditions;

	public TreeType(TreeCondition conditions) {
		this.conditions = conditions;
	}
}
