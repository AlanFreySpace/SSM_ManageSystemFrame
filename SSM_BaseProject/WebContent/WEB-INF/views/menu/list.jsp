<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="easyui-layout" data-options="fit:true">
    <!-- Begin of toolbar -->
    <div id="wu-toolbar">
        <div class="wu-toolbar-button">
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openAdd()" plain="true">添加</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-edit" onclick="openEdit()" plain="true">修改</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-remove" onclick="remove()" plain="true">删除</a>
        </div>
        <div class="wu-toolbar-search">
            <label>菜单名称：</label><input id="search-name" class="wu-text" style="width:100px">
            <a href="#" id="search-btn" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
        </div>
    </div>
    <!-- End of toolbar -->
    <table id="data-datagrid" class="easyui-treegrid" toolbar="#wu-toolbar"></table>
</div>

<style>
.selected{
	background:red;
}
</style>

<!-- Begin of easyui-dialog -->
<div id="add-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:450px; padding:10px;">
	<form id="add-form" method="post">
        <table>
            <tr>
                <td width="60" align="right">菜单名称:</td>
                <td><input type="text" name="name" class="wu-text easyui-validatebox" data-options="required:true,missingMessage:'请填写菜单名称'" /></td>
            </tr>
            <tr>
                <td align="right">上级菜单:</td>
                <td>
                    <select name="parentId" class="easyui-combobox" panelHeight="auto" style="width:268px">
                       <c:forEach items="${topList }" var="menu">
                       <option value="${menu.id }">${menu.name }</option>
                       </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td align="right">菜单URL:</td>
                <td><input type="text" name="url" class="wu-text" /></td>
            </tr>
            <tr>
                <td valign="top" align="right">菜单图标:</td>
                <td>
                	<input type="text" id="add-icon" name="icon" class="wu-text easyui-validatebox" data-options="required:true,missingMessage:'请填写菜单图标'"/>
                	<a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="selectIcon()" plain="true">选择</a>
                </td>
            </tr>
        </table>
    </form>
</div>
<!-- End of easyui-dialog -->

<!-- 选择图标弹窗 -->
<div id="select-icon-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:820px;height:550px; padding:10px;">
	<table id="icons-table" cellspacing="8">
		
	</table>
</div>

<script type="text/javascript">
	/**
	* Name 添加记录
	*/
	function add(){
		var validate=$("#add-form").form("validate");
		if(!validate){
			$.messager.alert("消息提醒","请检查你输入的数据!","warning");
		}
		var data=$("#add-form").serialize();
		$.ajax({
			url:'../admin/menu/add',
			dataType:'json',
			type:'post',
			data:data,
			success:function(data){
				if(data.type=='success'){
					$.messager.alert('信息提示','添加成功！','info');
					$('#add-dialog').dialog('close');
					$('#data-datagrid').treegrid('reload');
				}
				else
				{
					$.messager.alert('信息提示',data.msg,'warning');
				}
			}
		});
	}
	
	function selectIcon(){
		if($("#icons-table").children().length<=0){
			$.ajax({
				url:'../admin/menu/get_icons',
				dataType:'json',
				type:'post',
				success:function(data){
					if(data.type=='success'){
						var icons=data.content;
						var table='';
						for(var i=0;i<icons.length;i++){
							var tbody='<td class="icon-td"><a onclick="selected(this)" href="javascript:void(0)" class="'+icons[i]+'">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></td>'
							if(i==0){
								table+='<tr>'+tbody;
								continue;
							}
							if((i+1)%24==0){
								table+=tbody+'</tr><tr>';
								continue;
							}
							table+=tbody;
						}
						table+='</tr>';
						$("#icons-table").append(table);
					}
					else
					{
						$.messager.alert('信息提示',data.msg,'warning');
					}
				}
				
			});
		}
		
		$('#select-icon-dialog').dialog({
			closed: false,
			modal:true,
            title: "选择icon信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: function(){
                	var icon=$(".selected a").attr('class');
                	$("#add-icon").val(icon);
                	$('#select-icon-dialog').dialog('close'); 
                }
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#select-icon-dialog').dialog('close');                    
                }
            }]
        });
	}
	
	function selected(e){
		$(".icon-td").removeClass('selected');
		$(e).parent("td").addClass('selected');
	}
	
	/**
	* Name 修改记录
	*/
	function edit(){
		$('#wu-form-2').form('submit', {
			url:'',
			success:function(data){
				if(data){
					$.messager.alert('信息提示','提交成功！','info');
					$('#wu-dialog-2').dialog('close');
				}
				else
				{
					$.messager.alert('信息提示','提交失败！','info');
				}
			}
		});
	}
	
	/**
	* Name 删除记录
	*/
	function remove(){
		$.messager.confirm('信息提示','确定要删除该记录？', function(result){
			if(result){
				var items = $('#wu-datagrid-2').datagrid('getSelections');
				var ids = [];
				$(items).each(function(){
					ids.push(this.productid);	
				});
				//alert(ids);return;
				$.ajax({
					url:'',
					data:'',
					success:function(data){
						if(data){
							$.messager.alert('信息提示','删除成功！','info');		
						}
						else
						{
							$.messager.alert('信息提示','删除失败！','info');		
						}
					}	
				});
			}	
		});
	}
	
	/**
	* Name 打开添加窗口
	*/
	function openAdd(){
		$('#add-form').form('clear');
		$('#add-dialog').dialog({
			closed: false,
			modal:true,
            title: "添加菜单信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: add
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#add-dialog').dialog('close');                    
                }
            }]
        });
	}
	
	/**
	* Name 打开修改窗口
	*/
	function openEdit(){
		$('#wu-form-2').form('clear');
		var item = $('#wu-datagrid-2').datagrid('getSelected');
		//alert(item.productid);return;
		$.ajax({
			url:'',
			data:'',
			success:function(data){
				if(data){
					$('#wu-dialog-2').dialog('close');	
				}
				else{
					//绑定值
					$('#wu-form-2').form('load', data)
				}
			}	
		});
		$('#wu-dialog-2').dialog({
			closed: false,
			modal:true,
            title: "修改信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: edit
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#wu-dialog-2').dialog('close');                    
                }
            }]
        });
	}	
	
	$("#search-btn").click(function(){
		$('#data-datagrid').treegrid('reload',{
			name:$("#search-name").val()
		});
	});
	
	
	/**
	* Name 载入数据
	*/
	$('#data-datagrid').treegrid({
		url:'../admin/menu/list',	
		rownumbers:true,
		singleSelect:true,
		pageSize:20,           
		pagination:true,
		multiSort:true,
		fitColumns:true,
		idField:'id',
		treeField:'name',
		fit:true,
		columns:[[
			{ field:'name',title:'菜单名称',width:100,sortable:true},
			{ field:'url',title:'菜单URL',width:180,sortable:true},
			{ field:'icon',title:'图标icon',width:100}
		]]
	});
</script>