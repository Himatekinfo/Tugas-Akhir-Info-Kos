<?php

class EtoHelper
{
	public static function isEmpty($value,$trim=true)
	{
		return $value===null || $value===array() || $value==='' || trim($value) ==='-' || $trim && is_scalar($value) && trim($value)==='';
	}
}

?>
