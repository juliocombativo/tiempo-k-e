$(function() {
  (function($) {
	$.extend($, {
		kanban : function() {
			var $projects = $('#my-projects'),
				$projectButtons = $('#project-buttons'),
				$projectList = $('#project-list'),
				$projectForm = $('#new-project'),
				$taskForm = $('#task-info'),
				$messages = $('#up-message');
			
			$.extend(this, {
				configure : function() {
					$.ajaxSetup({headers: { 
						Accept : "application/json"
					}});
					
					this.apiUrl = $('a.api-url').attr('href');
				}, api : function(url) {
					return this.apiUrl + '/' + url;
				}, elementId : function(element) {
					var id = $(element).attr('id');
					return id.substring(id.indexOf('-') + 1);
				}, initialize : function() {
					var self = this;
					this.updateProjects();
					
					$projectList.on('change-project', function(event, project) {
						$.get(self.api('project/' + project.id + '/dashboard'), function(lanes) {
							$projectList.find('.project').remove();
							$projectList.append('<div id="project-' + project.id + '" class="project"></div>');
							$project = $projectList.find('.project');
							for(var i = 0; i < lanes.lane.length; i++) {
								var lane = lanes.lane[i];
								$project.append('<div class="ui-state-default lane" id="lane-' + lane.id + '"><h4 class="lane-title">' + lane.name + '</h4></div>');
							}
							$projectList.find('.project').css('width', (140 * lanes.lane.length) + 'px');
						});
					});
					$projects.on('add-project', function() {
						$projectForm.trigger('clear-data').dialog('open');
					}).on('add-task', function() {
						$taskForm.trigger('clear-data').dialog('open');
					});
					
					$buttons = $('#project-buttons');
					
					$projectButtons.find('a').button();
					$projectButtons.find('a.add-project').click(function() {
						$projects.trigger('add-project');
					});
					$projectButtons.find('a.add-task').click(function() {
						$projects.trigger('add-task');
					});
					
					$projectList.find('.project').each(function() {
						self.bindProjectEvents($(this));
					});
					
					$messages.on('show-message', function(event, message) {
						$(this).find('div').removeClass('ui-helper-hidden');
						$(this).find('div').text(message);
						$(this).removeClass('no-message');
						setTimeout(function() {
							$messages.trigger('hide-message');
						}, 5000);
					}).on('hide-message', function(event, message) {
						$(this).addClass('no-message');
						$(this).find('div').addClass('ui-helper-hidden');
					});
					
					$.get(this.api('user/' + this.elementId($('.user-logout-link')) + '/projects'), {}, function(projects) {
						if(projects) {
							console.log(projects);
							$projectButtons.find('.add-project').hide();
							$projectButtons.find('.add-task').show();
						} else {
							$messages.trigger('show-message', ['There are no projects assigned to this user.']);
							$projectButtons.find('.add-project').show();
							$projectButtons.find('.add-task').hide();
						}
					});
				}, updateProjects : function() {
					var self = this;
					
					$projectForm.dialog({
						modal: true,
						buttons : {
							'Create' : function() {
								$.post(self.api('projects/add'), $(this).find('form').serialize(), function(project) {
									$projectList.trigger('change-project', [project]);
								});
								$projects.find('#new-project').dialog('close');
							}
						}, autoOpen : false
					});
					
					$taskForm.dialog({
						modal : true,
						buttons : { 
							'Create' : function() {
								$.post(self.api('project/' + self.elementId($projects.find('.project'))) + '/tasks/add', 
										$('#task-info').find('form').serialize(), function(task) {
									$('#lane-' + task.station.id).trigger('new-task', task);
								});
								$projects.find('#task-info').dialog('close');
							}
						}, autoOpen : false
					});
				}, bindProjectEvents : function($project) {
					var self = this;
					
					$project.find('.task').draggable({
						containment : $project
					});
					$project.find('.lane').droppable({
						accept : $project.find('.task'),
						activeClass: 'ui-state-hover',
						hoverClass: 'ui-state-active',
						drop : function(event, ui) {
							if(ui.draggable.parent()[0] != this)
								$(this).trigger('task-added', [ui.draggable.parent(), ui.draggable]);
							else
								$(this).trigger('rearrange');
							return false;
						}
					}).on('task-added', function(event, from, task) {
						var $lane = $(this);
						$lane.find('.task').each(function() {
							console.log($(this), $(this).offset());
						});
						task.css('top', '4px').css('left', '0px');
						task.detach().appendTo(this);
						var url = 'project/' + self.elementId($(this).parents('.project')) + '/task/' + self.elementId(task) + '/status/' + self.elementId(from) +
							'/to/' + self.elementId(this);
						$.post(self.api(url), {}, function(data) {
							$lane.trigger('rearrange');
						});
					}).on('rearrange', function() {
						$(this).find('.task').each(function() {
							$(this).css('left', '0px');
							$(this).css('top', '4px');
						});
					});
				}
			});
			
			this.configure();
			this.initialize();
		}
	});
	$.kanban();
  })(jQuery);
});