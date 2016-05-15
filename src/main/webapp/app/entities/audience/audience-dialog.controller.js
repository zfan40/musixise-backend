(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('AudienceDialogController', AudienceDialogController);

    AudienceDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Audience'];

    function AudienceDialogController ($scope, $stateParams, $uibModalInstance, entity, Audience) {
        var vm = this;
        vm.audience = entity;
        vm.load = function(id) {
            Audience.get({id : id}, function(result) {
                vm.audience = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('musixiseApp:audienceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.audience.id !== null) {
                Audience.update(vm.audience, onSaveSuccess, onSaveError);
            } else {
                Audience.save(vm.audience, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
