<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Helper
 *
 * @author Blue Spy
 */
class Helper {
	public static function returnData($data, $statusCode=200, $print = true)
	{
		$status = array("statusCode"=>$statusCode);
		switch($statusCode)
		{
			case 201: { $status = array_merge(array("status"=>"Created"), $status); break; }
			case 204: { $status = array_merge(array("status"=>"No Content"), $status); break; }
			case 400: { $status = array_merge(array("status"=>"Bad request"), $status); break; }
			case 500: {	$status = array_merge(array("status"=>"Internal error"), $status); break; }
			case 501: {	$status = array_merge(array("status"=>"Not implemented"), $status); break; }
			case 401:
			case 403:
			case 550: {	$status = array_merge(array("status"=>"Permission denied"), $status); break; }
			case 200: { $status = array_merge(array("status"=>"Success"), $status); break; }
			default: { $status = array_merge(array("status"=>"Unidentified error"), $status); break; }
		}
		
		$msg = CJSON::encode(array_merge($status, $data));
		if($print)
			echo $msg;
		return $msg;
	}

	public static function printMethods($class, $exclude = array('index'))
	{
		foreach(get_class_methods($class) as $method)
		{
			if(substr($method,0,6) == "action" && $method!="actions")
			{
				if(!in_array(strtolower(substr($method,6)), $exclude))
				{
					echo substr($method,6);
					echo "(";
					Helper::printParameters($class, $method);
					echo ")";
					echo "<br />";
				}
			}
		}
	}

	public static function printParameters($class, $method)
	{
		$r = new ReflectionMethod($class, $method);
		$params = $r->getParameters();
		$asFirst=true;
		foreach ($params as $param) {
			if($asFirst)
			{
				$asFirst = !$asFirst;
			} else {
				echo ", ";
			}
			//$param is an instance of ReflectionParameter
			echo $param->getName();
			echo $param->isOptional();
		}
	}
}

?>
