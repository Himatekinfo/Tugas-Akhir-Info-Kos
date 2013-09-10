<?php
/* @var $this IncidentImageController */
/* @var $dataProvider CActiveDataProvider */

$this->breadcrumbs=array(
	'Incident Images',
);

$this->menu=array(
	array('label'=>'Create IncidentImage', 'url'=>array('create')),
	array('label'=>'Manage IncidentImage', 'url'=>array('admin')),
);
?>

<h1>Incident Images</h1>

<?php $this->widget('zii.widgets.CListView', array(
	'dataProvider'=>$dataProvider,
	'itemView'=>'_view',
)); ?>
