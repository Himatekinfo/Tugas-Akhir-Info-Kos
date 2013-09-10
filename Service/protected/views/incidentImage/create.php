<?php
/* @var $this IncidentImageController */
/* @var $model IncidentImage */

$this->breadcrumbs=array(
	'Incident Images'=>array('index'),
	'Create',
);

$this->menu=array(
	array('label'=>'List IncidentImage', 'url'=>array('index')),
	array('label'=>'Manage IncidentImage', 'url'=>array('admin')),
);
?>

<h1>Create IncidentImage</h1>

<?php $this->renderPartial('_form', array('model'=>$model)); ?>